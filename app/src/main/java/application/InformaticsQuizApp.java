package application;

import android.app.Application;

import models.Game;
import network.Client;
import network.Server;

/**
 * Created by andre
 */

public class InformaticsQuizApp extends Application {

    private Game game;

    private Server localServer;
    private Client localClient;

    public InformaticsQuizApp(){
        this.game = null;
        this.localServer = null;
        this.localClient = null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Server getLocalServer() {
        return localServer;
    }

    public void setLocalServer(Server localServer) {
        this.localServer = localServer;
    }

    public Client getLocalClient() {
        return localClient;
    }

    public void setLocalClient(Client localClient) {
        this.localClient = localClient;
    }

}
