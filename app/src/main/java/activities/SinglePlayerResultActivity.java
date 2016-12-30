package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;

import java.util.Date;

import models.Game;
import models.MySharedPreferences;
import models.PlayerData;
import models.SinglePlayerGameResult;
import models.SoundEffect;

/**
 * Created by andre
 */

public class SinglePlayerResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_single_player_result);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.single_player_game_result_text);

        Intent gameResultIntent = getIntent();
        Game game = (Game) gameResultIntent.getSerializableExtra("game");

        boolean gameResult = game.getResult();
        Date gameDate = new Date();
        int gameTotalQuestions = game.getnQuestions();
        int gameScore = game.getScore();
        int nRightAnswers = game.getnRightQuestions();
        double pRightAnswers = Math.round((double)nRightAnswers / gameTotalQuestions * 100.0);
        int nWrongAnswers = game.getnWrongQuestions();
        double pWrongAnswers = Math.round((double)nWrongAnswers / gameTotalQuestions * 100.0);
        int gameDifficulty = game.getDifficultyId();

        int gamePlayerScore = game.getScore();
        int gameTotalScore = game.getTotalScore();

        View layout = this.getWindow().getDecorView();
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
            double halfScore = game.getTotalScore() / 2;
            gameScore = ((int)halfScore - game.getScore()) * (-1);
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

        if(savedInstanceState==null) {
            PlayerData playerData = PlayerData.loadData(this);
            if (playerData != null) {
                playerData.setSinglePlayerPontuation(playerData.getSinglePlayerPontuation() + gameScore);
                playerData.setnRightAnswers(playerData.getnRightAnswers() + nRightAnswers);
                playerData.setTotalAnswers(playerData.getTotalAnswers() + gameTotalQuestions);
                playerData.saveData(this);

                (new SinglePlayerGameResult(gameDate, gameDifficulty, gameResult, gameScore,
                        nRightAnswers, nWrongAnswers)).save(getApplicationContext());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                SoundEffect.stopAllSounds();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
