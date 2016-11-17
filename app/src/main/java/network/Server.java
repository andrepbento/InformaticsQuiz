package network;

import android.util.Log;

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

/**
 * Created by andre on 16/11/2016.
 */

public class Server extends Thread {

    private String ip;
    private int listeningPort;

    private ServerSocket serverSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean full;

    private int nPlayers;

    List<Socket> clientSockets;
    //List<Game>

    public Server(int listeningPort, int nPlayers) {
        this.listeningPort = listeningPort;
        try {
            this.serverSocket = new ServerSocket(listeningPort);
            this.nPlayers = nPlayers;
            clientSockets = new ArrayList<>();
        } catch (IOException e) {
            Log.e("Server", e.getMessage());
        }
        this.ip = getLocalIpAddress();
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

    @Override
    public void run() {
        full = false;
        int playersCounter = 0;

        try {
            while (!full) {
                Socket s = null;
                s = serverSocket.accept();
                clientSockets.add(s);
                // lançar thread que atende clientes

                playersCounter++;

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

    }

    public void stopReceivingClients() { full = true; }

    class CommunicationThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }
}
