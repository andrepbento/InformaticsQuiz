package application;

import android.app.Application;

import activities.GameActivity;
import models.Game;
import network.Client;
import network.Server;

/**
 * Created by andre
 */

public class InformaticsQuizApp extends Application {
    private boolean inBackground = false;
    private boolean preferencesChanged = false;

    private Game game;
    private GameActivity.MyCountDownTimer myCountDownTimer;

    private Server localServer;
    private Client localClient;

    public InformaticsQuizApp(){
        this.game = null;
        this.localServer = null;
        this.localClient = null;
    }

    public boolean isInBackground() {
        return inBackground;
    }

    public void setInBackground(boolean inBackground) {
        this.inBackground = inBackground;
    }

    public boolean isPreferencesChanged() {
        return preferencesChanged;
    }

    public void setPreferencesChanged(boolean preferencesChanged) {
        this.preferencesChanged = preferencesChanged;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameActivity.MyCountDownTimer getMyCountDownTimer() {
        return myCountDownTimer;
    }

    public void setMyCountDownTimer(GameActivity.MyCountDownTimer myCountDownTimer) {
        this.myCountDownTimer = myCountDownTimer;
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
