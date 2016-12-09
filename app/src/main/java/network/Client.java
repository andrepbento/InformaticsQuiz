package network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import models.Game;
import models.MultiPlayerGameResult;
import models.PlayerData;

/**
 * Created by andre
 */

public class Client extends Thread {//AsyncTask<Void, Void, Void> {

    private String serverIp;
    private int serverPort;

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private PlayerData playerData;

    //private Game game;

    boolean running;

    public Client(Context context, String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        ConnectivityManager connMgr = (ConnectivityManager)	context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(context, "No network connection!", Toast.LENGTH_SHORT).show();
            ((Activity)context).finish();
            return;
        }

        playerData = PlayerData.loadData(context);

        this.start();
    }

    //@Override
    //protected Void doInBackground(Void... params) {

    @Override
    public void run() {
        running = true;

        try {
            clientSocket = new Socket(InetAddress.getByName(serverIp), serverPort);
            Log.e("Client", "Ligado");

            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            sendDataToServer(playerData);

            while (running) {

                Object read = in.readObject();

                if(read instanceof String) {
                    String str = (String) read;
                    // Comando qualquer
                } else if (read instanceof Game) {
                    //game = (Game) read;
                    //this.publishProgress("startGame");
                    // recebeu o jogo arracnar com o mesmo e no final enviar estatisticas
                } else if (read instanceof MultiPlayerGameResult) {
                    MultiPlayerGameResult mpgr = (MultiPlayerGameResult) read;
                }

            }

            out.close();
            in.close();
        } catch (IOException e) {
            Log.e("Client", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Client", e.getMessage());
        } /*finally {
            if(clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    Log.e("Client", e.getMessage());
                }
        }
        */

        //return null;
    }

    private void stopReceiving() { running = false; }

    private void sendDataToServer(Object data) {

        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
