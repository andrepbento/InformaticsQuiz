package network;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import models.Game;
import models.MultiPlayerGameResult;
import models.PlayerData;

/**
 * Created by andre on 16/11/2016.
 */

public class Client extends Thread {

    private String serverIp;
    private int serverPort;

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private PlayerData playerData;

    boolean running;

    public Client(Context context, String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        clientSocket = null;

        playerData = PlayerData.loadData(context);
    }

    @Override
    public void run() {
        running = true;

        try {
            clientSocket = new Socket(serverIp, serverPort);

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            sendDataToServer(playerData);

            while (running) {

                Object read = in.readObject();

                if(read instanceof String) {
                    String str = (String) read;
                    // Comando qualquer
                } else if (read instanceof Game) {
                    Game game = (Game) read;
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
