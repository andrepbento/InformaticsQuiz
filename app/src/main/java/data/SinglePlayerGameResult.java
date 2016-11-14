package data;

import java.util.Date;

/**
 * Created by andre on 12/11/2016.
 */

public class SinglePlayerGameResult extends GameResult {
    private int gameId = 0;
    private boolean gameResult;
    private int gameScore;
    private int nRightAnswers;
    private double pRightAnswers;
    private int nWrongAnswers;
    private double pWrongAnswers;

    public SinglePlayerGameResult(Date gameDate, int gameNQuestions, int gameDifficulty, int gameId,
                                  boolean gameResult, int gameScore, int nRightAnswers, double pRightAnswers,
                                  int nWrongAnswers, double pWrongAnswers) {
        super(gameDate, gameNQuestions, gameDifficulty);
        this.gameId = gameId;
        this.gameResult = gameResult;
        this.gameScore = gameScore;
        this.nRightAnswers = nRightAnswers;
        this.pRightAnswers = pRightAnswers;
        this.nWrongAnswers = nWrongAnswers;
        this.pWrongAnswers = pWrongAnswers;
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

    public double getpRightAnswers() {
        return pRightAnswers;
    }

    public int getnWrongAnswers() {
        return nWrongAnswers;
    }

    public double getpWrongAnswers() {
        return pWrongAnswers;
    }
}
