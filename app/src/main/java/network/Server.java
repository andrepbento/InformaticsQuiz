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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<MultiPlayerGameConnection> gameConnections;

    private Game game;

    private boolean full;
    public static int playersCounter;

    private List<PlayerData> playersDataList;
    private Map<Integer, Game> gameResults;
    private MultiPlayerGameResult multiPlayerGameResult;

    public Server(Activity activity, Game game) {
        if (!Connection.checkNetworkConnection(activity)) {
            Toast.makeText(activity, "No network connection!", Toast.LENGTH_SHORT).show();
            (activity).finish();
            return;
        }

        try {
            this.serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            Log.e("Server", e.toString());
            closeSocket();
        }
        this.gameConnections = new ArrayList<>();
        this.activity = activity;
        this.game = game;
        this.full = false;
        this.playersDataList = new ArrayList<>();
        this.gameResults = new HashMap<>();
        this.playersCounter = 0;
    }

    public void stopServer() {
        stopAcceptingClients();
        for(MultiPlayerGameConnection multiPlayerGameConnection : gameConnections)
            multiPlayerGameConnection.stopRunning();
        closeSocket();
    }

    private void closeSocket() {
        if(serverSocket != null)
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e("Server", e.toString());
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
            Log.e("Server", e.toString());
        }
        return "";
    }

    public int getServerPort() {
        return serverSocket.getLocalPort();
    }

    public int getPlayersConnected() {
        return playersCounter;
    }

    public int getnPlayers() {
        return game.getnPlayers();
    }

    public void checkReceivedAllGames() {
        if(playersDataList.size() <= gameResults.size())
            sendMultiPlayerGameResultToAllClients();
    }

    @Override
    protected Void doInBackground(Void... params) {
        receiveClients();

        for(MultiPlayerGameConnection multiPlayerGameConnection : gameConnections){
            try {
                multiPlayerGameConnection.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void receiveClients() {
        try {
            while (!full) {
                Socket s = serverSocket.accept();

                MultiPlayerGameConnection mpgc = new MultiPlayerGameConnection(playersCounter, s);
                mpgc.start();

                if (game.getnPlayers() <= playersCounter) {
                    stopAcceptingClients();
                    sendGameToAllClients();
                }
            }
        } catch(IOException e) {
            Log.e("Server", e.toString());
        }
    }

    public void stopAcceptingClients() {
        full = true;
    }

    private void sendGameToAllClients() {
        if(activity instanceof QRCodeActivity)
            activity.finish();
        for(MultiPlayerGameConnection multiPlayerGameConnection : gameConnections)
            multiPlayerGameConnection.sendMSGToClient(new MSG(Constants.MSG_CODE_GAME, game));
    }

    private void processMultiPlayerResult() {
        multiPlayerGameResult = new MultiPlayerGameResult(new Date(), game.getnQuestions(), game.getDifficultyId());
        for(int i = 0; i < game.getnPlayers(); i++) {
            if(gameResults.containsKey(i))
                multiPlayerGameResult.addPlayerInfo(playersDataList.get(i), gameResults.get(i));
            else
                multiPlayerGameResult.addPlayerInfo(playersDataList.get(i), null);
        }
    }

    private void sendMultiPlayerGameResultToAllClients() {
        processMultiPlayerResult();
        MSG msg = new MSG(Constants.MSG_CODE_MULTI_PLAYER_GAME_RESULT, multiPlayerGameResult);
        for(MultiPlayerGameConnection multiPlayerGameConnection : gameConnections) {
            multiPlayerGameConnection.sendMSGToClient(msg);
        }
    }

    class MultiPlayerGameConnection extends Thread {
        private Socket socket;

        private ObjectInputStream in;
        private ObjectOutputStream out;

        private boolean running;
        private int playerIndex;

        private MSG msg = null;

        public MultiPlayerGameConnection(int playerIndex, Socket socket) {
            gameConnections.add(this);
            this.running = true;
            this.playerIndex = playerIndex;
            playersCounter++;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();

                while(running) {
                    msg = (MSG)in.readObject();
                    switch(msg.getMsgCode()) {
                        case Constants.MSG_CODE_PLAYER_DATA:
                            final PlayerData playerData = msg.getPlayerData();
                            playersDataList.add(playerData);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((QRCodeActivity)activity).tvPlayersConnected.setText("Players connected: "
                                            +playersCounter+"/"+game.getnPlayers());
                                    Toast.makeText((QRCodeActivity) activity, "Jogador "+playerData.getName()
                                            +" ligado!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case Constants.MSG_CODE_GAME:
                            gameResults.put(playerIndex, msg.getGame());
                            checkReceivedAllGames();
                            break;
                        case Constants.MSG_CODE_ANSWER:
                            MSG msgTmp = msg;
                            sendMSGToOtherClients(new MSG(Constants.MSG_CODE_INFO, msgTmp.getMsg()));
                            break;
                    }
                }
                in.close();
                out.close();
            } catch (ClassNotFoundException e) {
                Log.e("Server", e.toString());
            } catch (IOException e) {
                Log.e("Server", e.toString());
            } catch (Exception e) {
                Log.e("Server", e.toString());
            } finally {
                closeSocket();
            }
        }

        private void closeSocket() {
            if(socket!=null)
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e("Server", e.toString());
                }
        }

        public void stopRunning() {
            this.running = false;
            closeSocket();
        }

        public void sendMSGToClient(final MSG msg) {
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

        public void sendMSGToOtherClients(final MSG msg) {
            for(MultiPlayerGameConnection multiPlayerGameConnection : gameConnections)
                if(!multiPlayerGameConnection.equals(this))
                    multiPlayerGameConnection.sendMSGToClient(msg);
        }
    }
}
