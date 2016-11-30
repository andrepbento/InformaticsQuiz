package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;

import java.util.Date;

import models.PlayerData;
import models.SinglePlayerGameResult;
import models.SoundEffect;
import utils.InformaticsQuizHelper;

public class SinglePlayerGameResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_game_result);

        Intent gameResultIntent = getIntent();
        boolean gameResult = gameResultIntent.getBooleanExtra("gameResult", false);
        Date gameDate = new Date();
        int gameTotalQuestions = gameResultIntent.getIntExtra("gameTotalQuestions", 0);
        int gameScore = gameResultIntent.getIntExtra("gameScore", 0);
        int nRightAnswers = gameResultIntent.getIntExtra("nRightAnswers", 0);
        double pRightAnswers = Math.round((double)nRightAnswers / gameTotalQuestions * 100.0);
        int nWrongAnswers = gameResultIntent.getIntExtra("nWrongAnswers", 0);
        double pWrongAnswers = Math.round((double)nWrongAnswers / gameTotalQuestions * 100.0);
        int gameDifficulty = gameResultIntent.getIntExtra("gameDifficulty", 0);

        int gamePlayerScore = gameResultIntent.getIntExtra("gamePlayerScore",0);
        int gameTotalScore = gameResultIntent.getIntExtra("gameTotalScore",0);

        View layout = this.getWindow().getDecorView();

        //         android:id="@+id/iv_game_result" ************************************************

        TextView tvGameResult = (TextView) findViewById(R.id.tv_game_result);
        TextView tvScoreAdded = (TextView) findViewById(R.id.tv_score_added);
        if(gameResult) {
            tvGameResult.setText("PASSAS-TE! Parabéns");
            layout.setBackground(getResources().getDrawable(R.drawable.green_white_gradient));
            tvScoreAdded.setText("+ " + String.valueOf(gameScore));
            SoundEffect.playWinGameSound();
        } else {
            tvGameResult.setText("CHUMBAS-TE! Tens que te esforçar mais...");
            layout.setBackground(getResources().getDrawable(R.drawable.red_white_gradient));
            tvScoreAdded.setText(String.valueOf(gameScore));
            SoundEffect.playLoseGameSound();
        }
        tvScoreAdded.setText(tvScoreAdded.getText() + "   " + gamePlayerScore + "/" + gameTotalScore);

        ((TextView) findViewById(R.id.tv_game_date)).setText(gameDate.toString());
        ((TextView) findViewById(R.id.tv_n_right_answers)).setText(String.valueOf(nRightAnswers));
        ((TextView) findViewById(R.id.tv_perc_right_answers)).setText(String.valueOf((int)pRightAnswers) + "%");
        ((TextView) findViewById(R.id.tv_n_wrong_answers)).setText(String.valueOf(nWrongAnswers));
        ((TextView) findViewById(R.id.tv_perc_wrong_answers)).setText(String.valueOf((int)pWrongAnswers) + "%");
        ((TextView) findViewById(R.id.tv_game_n_questions)).setText(String.valueOf(gameTotalQuestions));

        String[] diffArray = this.getResources().getStringArray(R.array.difficulty);
        ((TextView) findViewById(R.id.tv_game_difficulty)).setText(diffArray[gameDifficulty]);

        PlayerData playerData = PlayerData.loadData(this);
        if(playerData != null) {
            playerData.setPontuation(playerData.getPontuation() + gameScore);
            playerData.setnRightAnswers(playerData.getnRightAnswers() + nRightAnswers);
            playerData.setnWrongAnswers(playerData.getnWrongAnswers() + nWrongAnswers);
            playerData.setTotalAnswers(playerData.getTotalAnswers() + gameTotalQuestions);
            playerData.saveData(this);

            InformaticsQuizHelper dbI = new InformaticsQuizHelper(this);

            dbI.create();

            if (dbI.open()) {
                int gameId = (dbI.getLastSinglePlayerGameId() + 1);

                SinglePlayerGameResult spgr = new SinglePlayerGameResult(gameDate, gameDifficulty, gameId,
                        gameResult, gameScore, nRightAnswers, nWrongAnswers);

                dbI.insertSinglePlayerGameResult(spgr);

                dbI.close();
            }
        }
    }

    public void onButtonGoToInicialMenu(View view) {
        SoundEffect.stopAllSounds();
        onBackPressed();
    }
}
