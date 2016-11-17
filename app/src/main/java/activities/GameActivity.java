package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import java.util.concurrent.TimeUnit;

import interfaces.PublicConstantValues;
import models.Game;
import models.Question;
import models.SoundEffect;

public class GameActivity extends Activity {

    ImageView ivGreenCircle,  ivYellowCircle, ivRedCircle;
    TextView tvQuestionTimer, tvQuestionNumber, tvQuestionDesc;
    Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;

    Game game;

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
        tvQuestionNumber = (TextView)findViewById(R.id.tvQuestionNumber);
        tvQuestionDesc = (TextView)findViewById(R.id.tvQuestionDescription);
        btnAnswerA = (Button)findViewById(R.id.btnAnswerA);
        btnAnswerB = (Button)findViewById(R.id.btnAnswerB);
        btnAnswerC = (Button)findViewById(R.id.btnAnswerC);
        btnAnswerD = (Button)findViewById(R.id.btnAnswerD);

        Intent received_intent = getIntent();
        if(received_intent != null) {
            String difficulty = received_intent.getStringExtra("difficulty");
            int nQuestions = received_intent.getIntExtra("nQuestions",0);

            game = new Game(this, difficulty, nQuestions);

            actualizaInterface();
        }
    }

    public void onButtonRespostaClick(View v) throws InterruptedException {
        switch (v.getId()) {
            case R.id.btnAnswerA:
                if(game.checkAnswer("A")){
                    SoundEffect.playRightAnswerSound();
                } else {
                    SoundEffect.playWrongAnswerSound();
                }
                break;
            case R.id.btnAnswerB:
                if(game.checkAnswer("B")){
                    SoundEffect.playRightAnswerSound();
                } else {
                    SoundEffect.playWrongAnswerSound();
                }
                break;
            case R.id.btnAnswerC:
                if(game.checkAnswer("C")){
                    SoundEffect.playRightAnswerSound();
                } else {
                    SoundEffect.playWrongAnswerSound();
                }
                break;
            case R.id.btnAnswerD:
                if(game.checkAnswer("D")){
                    SoundEffect.playRightAnswerSound();
                } else {
                    SoundEffect.playWrongAnswerSound();
                }
                break;
        }

        nextQuestion();
    }

    public void nextQuestion() {
        cdt.cancel();

        if(game.checkEnd()) {
            Intent gameResultIntent = new Intent(GameActivity.this, SinglePlayerGameResultActivity.class);
            gameResultIntent.putExtra("gameResult", game.getResult());
            gameResultIntent.putExtra("gameTotalQuestions", game.getnQuestions());
            if(game.getResult())
                gameResultIntent.putExtra("gameScore", game.getScore());
            else {
                double halfScore = game.getTotalScore() / 2;
                int loseScore = ((int)halfScore - game.getScore()) * (-1);
                gameResultIntent.putExtra("gameScore", loseScore);
            }
            gameResultIntent.putExtra("nRightAnswers", game.getnRightQuestions());
            gameResultIntent.putExtra("nWrongAnswers", game.getnWrongQuestions());
            gameResultIntent.putExtra("gameDifficulty", game.getDifficultyInt());
            startActivity(gameResultIntent);
            finish();
        } else {
            actualizaInterface();
        }
    }

    private void actualizaInterface() {
        Question question = game.getNextQuestion();

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

        int questionNumViewValue = game.getCurrentQuestionNum()+1;
        tvQuestionNumber.setText(getResources().getString(R.string.tv_question_number_text)
                + " " + questionNumViewValue);
        tvQuestionDesc.setText(question.getQuestionDesc());

        btnAnswerA.setText("A: " + question.getAnswerA());
        btnAnswerB.setText("B: " + question.getAnswerB());
        btnAnswerC.setText("C: " + question.getAnswerC());
        btnAnswerD.setText("D: " + question.getAnswerD());

        cdt = new CountDownTimer(game.getQuestionTime(), PublicConstantValues.tickTime) {

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

    public void onButtonGiveUp(View view) {
        cdt.cancel();
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
