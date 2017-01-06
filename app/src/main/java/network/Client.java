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
import java.net.UnknownHostException;

import activities.GameActivity;
import activities.MultiPlayerResultActivity;
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

    private InetAddress serverIp;
    private int serverPort;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean running;

    private MSG msg = null;

    public Client(Activity activity, String serverIp, int serverPort) {
        this.activity = activity;
        try {
            this.serverIp = InetAddress.getByName(serverIp);
        } catch (UnknownHostException e) {
            Toast.makeText(activity, "No network connection!", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
        this.serverPort = serverPort;

        ConnectivityManager connMgr = (ConnectivityManager)	activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(activity, "No network connection!", Toast.LENGTH_SHORT).show();
            (activity).finish();
            return;
        }
    }

    @Override
    public void run() {
        running = true;

        try {
            socket = new Socket(serverIp, serverPort);

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());

            sendMsgToServer(new MSG(Constants.MSG_CODE_PLAYER_DATA, PlayerData.loadData(activity)));

            while (running) {
                msg = (MSG)in.readObject();
                switch (msg.getMsgCode()) {
                    case Constants.MSG_CODE_GAME:
                        Game game = msg.getGame();
                        Intent startGame = new Intent(activity, GameActivity.class);
                        startGame.putExtra("game", game);
                        activity.startActivity(startGame);
                        break;
                    case Constants.MSG_CODE_MULTI_PLAYER_GAME_RESULT:
                        Intent multiPlayerResultActivityIntent = new Intent(activity, MultiPlayerResultActivity.class);
                        MultiPlayerGameResult multiPlayerGameResult = msg.getMultiPlayerGameResult();
                        multiPlayerResultActivityIntent.putExtra("multiPlayerGameResultID",
                                multiPlayerGameResult.getMultiPlayerGameResultID());
                        multiPlayerGameResult.save(activity);
                        activity.startActivity(multiPlayerResultActivityIntent);
                        break;
                    case Constants.MSG_CODE_INFO:
                        final String receivedMsg = msg.getMsg();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, receivedMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }

            out.close();
            in.close();
        } catch (IOException e) {
            Log.e("Client", e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(Exception e) {
            Log.e("Client", e.toString());
        } finally {
            closeSocket();
        }
    }

    private void closeSocket() {
        if(socket != null)
            try {
                socket.close();
            } catch (IOException e) {
                Log.e("Client", e.toString());
            }
    }

    private void stopReceiving() {
        running = false;
    }

    public void stopClient() {
        stopReceiving();
        closeSocket();
    }

    public void sendMsgToServer(final MSG msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeObject(msg);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
