package models;

import java.util.Date;
import java.util.List;

/**
 * Created by andre
 */

public class MultiPlayerGameResult extends GameResult {

    private int nPlayers;
    List<PlayerData> playerDatasList;

    public MultiPlayerGameResult(Date gameDate, int gameNQuestions, int gameDifficulty) {
        super(gameDate, gameNQuestions, gameDifficulty);
    }
}
