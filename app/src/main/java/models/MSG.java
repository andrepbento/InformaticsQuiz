package models;

import java.io.Serializable;

/**
 * Created by andre
 */

public class MSG implements Serializable {
    static final long serialVersionUID = 1010L;

    private int msgCode;
    private String msg = null;
    private PlayerData playerData = null;
    private Game game = null;
    private MultiPlayerGameResult multiPlayerGameResult = null;

    public MSG(int msgCode) {
        this.msgCode = msgCode;
    }

    public MSG(int msgCode, String msg) {
        this.msgCode = msgCode;
        this.msg = msg;
    }

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

    public String getMsg() { return msg; }

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
