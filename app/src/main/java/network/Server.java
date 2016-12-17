package network;

import android.app.Activity;
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
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import activities.QRCodeActivity;
import interfaces.Constants;
import models.Game;
import models.MSG;
import models.MultiPlayerGameResult;
import models.PlayerData;

/**
 * Created by andre
 */

public class Server extends AsyncTask<Void, Void, Void> {

    private Activity activity;

    private ServerSocket serverSocket;
    private List<Socket> clientSockets;
    private List<MultiPlayerGameConnection> gameConnections;

    private Game game;

    private boolean full;

    private MultiPlayerGameResult multiPlayerGameResult;

    public Server(Activity activity, int nPlayers, Game game) {
        if (!Connection.checkNetworkConnection(activity)) {
            Toast.makeText(activity, "No network connection!", Toast.LENGTH_SHORT).show();
            ((Activity)activity).finish();
            return;
        }

        try {
            this.serverSocket = new ServerSocket(Constants.serverListeningPort);
            clientSockets = new ArrayList<>();
            gameConnections = new ArrayList<>();
        } catch (IOException e) {
            Log.e("Server", e.getMessage());
            closeSocket();
        }
        this.activity = activity;
        this.game = game;
        this.full = false;

        this.execute();
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

    // public int getListeningPort() { return serverSocket.getLocalPort(); }

    public int getnPlayers() {
        return game.getnPlayers();
    }

    @Override
    protected Void doInBackground(Void... params) {

        receiveClients();

        try {
            for (MultiPlayerGameConnection mpgc : gameConnections)
                mpgc.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void receiveClients() {
        int playersCounter = 0;

        try {
            while (!full) {
                Socket s = serverSocket.accept();
                clientSockets.add(s);

                MultiPlayerGameConnection mpgc = new MultiPlayerGameConnection(playersCounter);
                mpgc.start();
                gameConnections.add(mpgc);

                playersCounter++;

                final int finalPlayersCounter = playersCounter;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((QRCodeActivity)activity).tvPlayersConnected.setText("Players connected: " + finalPlayersCounter
                                + "/" + game.getnPlayers());
                    }
                });

                if (game.getnPlayers() <= playersCounter) {
                    stopAcceptingClients();
                    sendGameToAllClients();
                }
            }
        } catch(IOException e) {
            Log.e("Server", e.getMessage());
        }
    }

    public void stopAcceptingClients() { full = true; }

    private void sendGameToAllClients() {
        for(MultiPlayerGameConnection mpgc : gameConnections)
            mpgc.sendMSGToClient(new MSG(Constants.MSG_CODE_GAME, game));
    }

    private void sendMultiPlayerGameResultToAllClients() {
        multiPlayerGameResult = new MultiPlayerGameResult(new Date(), game.getnQuestions(), game.getDifficultyId());
        MSG msg = new MSG(Constants.MSG_CODE_MULTI_PLAYER_GAME_RESULT, multiPlayerGameResult);
        for(MultiPlayerGameConnection mpgc : gameConnections) {
            mpgc.sendMSGToClient(msg);
            mpgc.stopRunning();
        }
    }

    class MultiPlayerGameConnection extends Thread {

        private ObjectInputStream in;
        private ObjectOutputStream out;

        private boolean running;
        private int playerIndex;

        public MultiPlayerGameConnection(int playerIndex) {
            this.running = true;
            this.playerIndex = playerIndex;
        }

        public void stopRunning() {
            this.running = false;
        }

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(clientSockets.get(playerIndex).getInputStream());
                out = new ObjectOutputStream(clientSockets.get(playerIndex).getOutputStream());

                while(running) {
                    Object obj = in.readObject();
                    if(obj instanceof MSG) {
                        MSG msg = (MSG)obj;
                        switch(msg.getMsgCode()) {
                            case Constants.MSG_CODE_PLAYER_DATA:
                                final PlayerData pd = msg.getPlayerData();
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(activity, "Jogador " + pd.getName() + " ligado!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case Constants.MSG_CODE_GAME:
                                // Guardar o jogo associando-o ao jogador em causa (playerIndex)
                                throw new Exception("Por implementar case Constants.MSG_CODE_GAME");
                                // break;
                        }
                    }
                }

                in.close();
                out.close();
            } catch (ClassNotFoundException e) {
                Log.e("Server", e.getMessage());
            } catch (IOException e) {
                Log.e("Server", e.getMessage());
            } catch (Exception e) {
                Log.e("Server", e.getMessage());
            }
        }

        private void sendMSGToClient(MSG msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
