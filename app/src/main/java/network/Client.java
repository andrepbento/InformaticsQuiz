package network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import models.Game;
import models.MultiPlayerGameResult;
import models.PlayerData;

/**
 * Created by andre on 16/11/2016.
 */

public class Client extends AsyncTask<Void, String, Void> {

    private String serverIp;
    private int serverPort;

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private PlayerData playerData;

    //private Game game;

    boolean running;

    public Client(Context context, String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        playerData = PlayerData.loadData(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        running = true;

        try {
            clientSocket = new Socket(InetAddress.getByName(serverIp), serverPort);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            sendDataToServer(playerData);

            while (running) {

                Object read = in.readObject();

                if(read instanceof String) {
                    String str = (String) read;
                    // Comando qualquer
                } else if (read instanceof Game) {
                    //game = (Game) read;
                    this.publishProgress("startGame");
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
        } finally {
            if(clientSocket != null)
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    Log.e("Client", e.getMessage());
                }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... args) {

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
