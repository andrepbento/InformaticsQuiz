package models;

import java.util.Date;

/**
 * Created by andre on 12/11/2016.
 */

public class GameResult {
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
