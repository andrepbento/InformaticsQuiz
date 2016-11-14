package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andre.informaticsquiz.Game;
import com.example.andre.informaticsquiz.R;
import com.example.andre.informaticsquiz.SoundEffect;

import data.Question;

public class GameActivity extends Activity {

    ImageView ivGreenCircle,  ivYellowCircle, ivRedCircle;
    TextView tvQuestionNumber, tvQuestionDesc;
    Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getActionBar().hide();

        new SoundEffect(this);

        ivGreenCircle = (ImageView) findViewById(R.id.iv_green_circle);
        ivYellowCircle = (ImageView) findViewById(R.id.iv_yellow_circle);
        ivRedCircle = (ImageView) findViewById(R.id.iv_red_circle);
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

        proximaPergunta();
    }

    public void proximaPergunta() {
        if(game.checkEnd()) {
            Intent gameResultIntent = new Intent(GameActivity.this, SinglePlayerGameResultActivity.class);
            gameResultIntent.putExtra("gameResult", game.getResult());
            gameResultIntent.putExtra("gameTotalQuestions", game.getnQuestions());
            gameResultIntent.putExtra("gameScore", game.getScore());
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

        switch (game.getCurrentQuestion().getQuestionDifInteger()) {
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
        tvQuestionNumber.setText(getResources().getString(R.string.pergunta_numero_text)
                + " " + questionNumViewValue);
        tvQuestionDesc.setText(question.getQuestionDesc());

        btnAnswerA.setText("A: " + question.getAnswerA());
        btnAnswerB.setText("B: " + question.getAnswerB());
        btnAnswerC.setText("C: " + question.getAnswerC());
        btnAnswerD.setText("D: " + question.getAnswerD());
    }

    public void onButtonGiveUp(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
