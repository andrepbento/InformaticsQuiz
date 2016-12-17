package models;

import java.util.Date;

/**
 * Created by andre
 */

public class SinglePlayerGameResult extends GameResult {

    private int gameId = 0;
    private boolean gameResult;
    private int gameScore;
    private int nRightAnswers;
    private int nWrongAnswers;

    public SinglePlayerGameResult(Date gameDate, int gameDifficulty, int gameId, boolean gameResult,
                                  int gameScore, int nRightAnswers, int nWrongAnswers) {
        super(gameDate, nRightAnswers + nWrongAnswers, gameDifficulty);
        this.gameId = gameId;
        this.gameResult = gameResult;
        this.gameScore = gameScore;
        this.nRightAnswers = nRightAnswers;
        this.nWrongAnswers = nWrongAnswers;
    }

    public int getGameId() {
        return gameId;
    }

    public boolean getGameResult() {
        return gameResult;
    }

    public int getGameScore() {
        return gameScore;
    }

    public int getnRightAnswers() {
        return nRightAnswers;
    }

    public int getnWrongAnswers() {
        return nWrongAnswers;
    }

    public double getpRightAnswers() { return Math.round((double)nRightAnswers / gameNQuestions * 100.0); }

    public double getpWrongAnswers() { return Math.round((double)nWrongAnswers / gameNQuestions * 100.0); }
}
