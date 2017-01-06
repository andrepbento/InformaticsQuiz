package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by andre
 */

public class GameResult implements Serializable {
    static final long serialVersionUID = 1010L;

    protected Date gameDate;
    protected int gameNQuestions;
    protected int gameDifficulty;

    public GameResult(Date gameDate, int gameNQuestions, int gameDifficulty) {
        this.gameDate = gameDate;
        this.gameNQuestions = gameNQuestions;
        this.gameDifficulty = gameDifficulty;
    }

    public Date getGameDate() {
        return gameDate;
    }

    public int getGameNQuestions() {
        return gameNQuestions;
    }

    public int getGameDifficulty() {
        return gameDifficulty;
    }
}
