package network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import activities.GameActivity;
import interfaces.Constants;
import models.Game;
import models.MSG;
import models.MultiPlayerGameResult;
import models.PlayerData;

/**
 * Created by andre
 */

public class Client extends Thread {

    private Activity activity;

    private String serverIp;
    private int serverPort;

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    boolean running;

    public Client(Activity activity, String serverIp, int serverPort) {
        this.activity = activity;
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        ConnectivityManager connMgr = (ConnectivityManager)	activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(activity, "No network connection!", Toast.LENGTH_SHORT).show();
            ((Activity)activity).finish();
            return;
        }

        this.start();
    }

    @Override
    public void run() {
        running = true;

        try {
            clientSocket = new Socket(InetAddress.getByName(serverIp), serverPort);
            Log.e("Client", "Ligado");

            out = new ObjectOutputStream(clientSocket.getOutputStream());

            sendDataToServer(new MSG(Constants.MSG_CODE_PLAYER_DATA, PlayerData.loadData(activity)));

            while (running) {
                in = new ObjectInputStream(clientSocket.getInputStream());

                Object obj = in.readObject();
                if(obj instanceof MSG) {
                    MSG msg = (MSG)obj;
                    switch (msg.getMsgCode()) {
                        case Constants.MSG_CODE_GAME:
                            Game game = msg.getGame();
                            Intent startGame = new Intent(activity, GameActivity.class);
                            startGame.putExtra("game", game);
                            activity.startActivity(startGame);
                            break;
                        case Constants.MSG_CODE_MULTI_PLAYER_GAME_RESULT:
                            MultiPlayerGameResult mpgr = msg.getMultiPlayerGameResult();
                            mpgr.save(activity);
                            stopReceiving();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            Log.e("Client", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Client", e.getMessage());
        } finally {
            if(clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    Log.e("Client", e.getMessage());
                }
        }
    }

    private void stopReceiving() throws IOException {
        running = false;
        out.close();
        in.close();
    }

    private void sendDataToServer(Object data) {
        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
