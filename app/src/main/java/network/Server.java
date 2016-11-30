package network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import activities.QRCodeActivity;
import interfaces.PublicConstantValues;
import models.Game;

/**
 * Created by andre
 */

public class Server extends AsyncTask<Void, Void, Void> {

    private int listeningPort;

    private ServerSocket serverSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean full;

    protected QRCodeActivity context;

    private int nPlayers;
    private Game game;

    List<Socket> clientSockets;

    public Server(Context context, int nPlayers, Game game) {
        ConnectivityManager connMgr = (ConnectivityManager)	context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(context, "No network connection!", Toast.LENGTH_LONG).show();
            //context.finish();
            return;
        }

        this.listeningPort = PublicConstantValues.listeningPort;
        try {
            this.serverSocket = new ServerSocket(listeningPort);
            this.nPlayers = nPlayers;
            clientSockets = new ArrayList<>();
        } catch (IOException e) {
            Log.e("Server", e.getMessage());
            closeSocket();
        }
        this.context = (QRCodeActivity) context;
        this.game = game;
    }

    public void closeSocket() {
        if(serverSocket != null)
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e("Server", e.getMessage());
            }
        serverSocket = null;
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while(en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("Server", e.getMessage());
        }
        return "";
    }

    public int getListeningPort() { return listeningPort; }

    public void stopReceivingClients() { full = true; }

    @Override
    protected Void doInBackground(Void... params) {
        full = false;
        int playersCounter = 0;

        try {
            while (!full) {
                Socket s =  serverSocket.accept();
                clientSockets.add(s);

                playersCounter++;

                final int finalPlayersCounter = playersCounter;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.tvPlayersConnected.setText("Players connected: " + finalPlayersCounter
                                                                + "/" + nPlayers);
                    }
                });


                if (nPlayers <= playersCounter) {
                    stopReceivingClients();
                    // Envia o jogo criado ao respectivo jogador
                    // Ao receber ele sabe que tem que começar a jogar
                    // Fica à espera de receber os dados de resultado de cada um deles
                    // Caso algum desista esse evento é tratado como se perdesse com 0, fim da tabela
                }

            }
        } catch (IOException e) {
            Log.e("Server", e.getMessage());
        }
        return null;
    }

    public int getnPlayers() {
        return nPlayers;
    }
}
