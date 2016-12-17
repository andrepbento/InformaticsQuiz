package models;

import java.io.Serializable;

/**
 * Created by andre on 14/12/2016.
 */

public class MSG implements Serializable {
    private int msgCode;
    private PlayerData playerData = null;
    private Game game = null;
    private MultiPlayerGameResult multiPlayerGameResult = null;

    public MSG(int msgCode, PlayerData playerData) {
        this.msgCode = msgCode;
        this.playerData = playerData;
    }

    public MSG(int msgCode, Game game) {
        this.msgCode = msgCode;
        this.game = game;
    }

    public MSG(int msgCode, MultiPlayerGameResult multiPlayerGameResult) {
        this.msgCode = msgCode;
        this.multiPlayerGameResult = multiPlayerGameResult;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public Game getGame() {
        return game;
    }

    public MultiPlayerGameResult getMultiPlayerGameResult() {
        return multiPlayerGameResult;
    }
}
