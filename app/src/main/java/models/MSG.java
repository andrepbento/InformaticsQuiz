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

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public MultiPlayerGameResult getMultiPlayerGameResult() {
        return multiPlayerGameResult;
    }

    public void setMultiPlayerGameResult(MultiPlayerGameResult multiPlayerGameResult) {
        this.multiPlayerGameResult = multiPlayerGameResult;
    }
}
