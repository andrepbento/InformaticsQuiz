package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import java.util.concurrent.TimeUnit;

import interfaces.Constants;
import models.Game;
import models.MyVibrator;
import models.Question;
import models.SoundEffect;

/**
 * Created by andre
 */

public class GameActivity extends Activity {

    ImageView ivGreenCircle,  ivYellowCircle, ivRedCircle;
    TextView tvQuestionTimer, tvQuestionDesc;
    ProgressBar pbQuestionProgress;
    Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;

    private Game game;

    private CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getActionBar().hide();

        new SoundEffect(this);

        ivGreenCircle = (ImageView) findViewById(R.id.iv_green_circle);
        ivYellowCircle = (ImageView) findViewById(R.id.iv_yellow_circle);
        ivRedCircle = (ImageView) findViewById(R.id.iv_red_circle);
        tvQuestionTimer = (TextView) findViewById(R.id.tv_question_timer);
        pbQuestionProgress = (ProgressBar) findViewById(R.id.pbQuestionProgress);
        tvQuestionDesc = (TextView)findViewById(R.id.tvQuestionDescription);
        btnAnswerA = (Button)findViewById(R.id.btnAnswerA);
        btnAnswerB = (Button)findViewById(R.id.btnAnswerB);
        btnAnswerC = (Button)findViewById(R.id.btnAnswerC);
        btnAnswerD = (Button)findViewById(R.id.btnAnswerD);

        Intent receivedIntent = getIntent();
        if(receivedIntent != null) {
            game = (Game) receivedIntent.getSerializableExtra("game");
            updateUI();
            startTimer();
        }
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        InformaticsQuizApp iqa = (InformaticsQuizApp)getApplication();
        iqa.setGame(game);
    }
*/
    @Override
    public void onBackPressed() {
    }

    public void onButtonRespostaClick(View v) throws InterruptedException {
        stopTimer();

        final Button button = (Button)v;
        boolean answerResult = false;
        switch (v.getId()) {
            case R.id.btnAnswerA:
                answerResult = game.checkAnswer("A");
                break;
            case R.id.btnAnswerB:
                answerResult = game.checkAnswer("B");
                break;
            case R.id.btnAnswerC:
                answerResult = game.checkAnswer("C");
                break;
            case R.id.btnAnswerD:
                answerResult = game.checkAnswer("D");
                break;
        }

        final Drawable drawable = button.getBackground();

        if(answerResult) {
            SoundEffect.playRightAnswerSound();
            button.setBackgroundColor(getResources().getColor(R.color.green_soft));
        } else {
            SoundEffect.playWrongAnswerSound();
            button.setBackgroundColor(getResources().getColor(R.color.red_soft));
            new MyVibrator(getApplicationContext()).vibrate(Constants.VIBRATION_SHORT);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                button.setBackground(drawable);
                nextQuestion();
            }
        }, Constants.timeToNextQuestion);
    }

    public void nextQuestion() {
        if(game.checkEnd()) {
            if(game.getGameMode() == Constants.SP_MODE)
                setSinglePlayerGameResult();
            else if (game.getGameMode() == Constants.MP_MODE)
                setMultiPlayerGameResult();
            finish();
        } else {
            updateUI();
            startTimer();
        }
    }

    private void updateUI() {
        Question question = game.getCurrentQuestion();

        ivGreenCircle.setVisibility(View.INVISIBLE);
        ivYellowCircle.setVisibility(View.INVISIBLE);
        ivRedCircle.setVisibility(View.INVISIBLE);

        switch (game.getCurrentQuestion().getQuestionDif()) {
            case 1:
                ivGreenCircle.setVisibility(View.VISIBLE); break;
            case 2:
                ivGreenCircle.setVisibility(View.VISIBLE);
                ivYellowCircle.setVisibility(View.VISIBLE); break;
            case 3:
                ivGreenCircle.setVisibility(View.VISIBLE);
                ivYellowCircle.setVisibility(View.VISIBLE);
                ivRedCircle.setVisibility(View.VISIBLE); break;
        }

        pbQuestionProgress.setMax(game.getnQuestions());
        pbQuestionProgress.setProgress(game.getCurrentQuestionNum()+1);
        tvQuestionDesc.setText(question.getQuestionDesc());

        btnAnswerA.setText("A: " + question.getAnswerA());
        btnAnswerB.setText("B: " + question.getAnswerB());
        btnAnswerC.setText("C: " + question.getAnswerC());
        btnAnswerD.setText("D: " + question.getAnswerD());
    }

    public void onButtonGiveUp(View view) {
        stopTimer();
        setSinglePlayerGameResult();
        finish();
    }

    private void startTimer() {
        if(game.getTimer()) {
            cdt = new CountDownTimer(game.getQuestionTime(), Constants.tickTime) {

                public void onTick(long millisUntilFinished) {
                    long millis = millisUntilFinished;
                    if(millis / 1000 <= 10)
                        tvQuestionTimer.setTextColor(Color.RED);
                    else
                        tvQuestionTimer.setTextColor(Color.BLACK);
                    String hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis));
                    tvQuestionTimer.setText("Time: "+ hms);
                    SoundEffect.playTickSound();
                }

                public void onFinish() {
                    Toast.makeText(getApplication(), "Times over!", Toast.LENGTH_SHORT).show();
                    game.checkAnswer("timeOver");
                    nextQuestion();
                }
            }.start();
        }
    }

    private void stopTimer() {
        if(game.getTimer())
            cdt.cancel();
    }

    private void setSinglePlayerGameResult() {
        Intent gameResultIntent = new Intent(GameActivity.this, SinglePlayerGameResultActivity.class);
        gameResultIntent.putExtra("gameResult", game.getResult());
        gameResultIntent.putExtra("gamePlayerScore", game.getScore());
        gameResultIntent.putExtra("gameTotalScore", game.getTotalScore());
        if(game.getResult())
            gameResultIntent.putExtra("gameScore", game.getScore());
        else {
            double halfScore = game.getTotalScore() / 2;
            int loseScore = ((int)halfScore - game.getScore()) * (-1);
            gameResultIntent.putExtra("gameScore", loseScore);
        }
        gameResultIntent.putExtra("nRightAnswers", game.getnRightQuestions());
        gameResultIntent.putExtra("nWrongAnswers", game.getnWrongQuestions());
        gameResultIntent.putExtra("gameDifficulty", game.getDifficultyId());
        gameResultIntent.putExtra("gameTotalQuestions", game.getnQuestions());
        startActivity(gameResultIntent);
    }

    private void setMultiPlayerGameResult() {
        try {

            throw new Exception("GAMEACTIVITY.setMultiPlayerGameResult() POR IMPLEMENTAR!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
