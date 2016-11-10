package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.Game;
import com.example.andre.informaticsquiz.R;

import data.Question;

public class GameActivity extends Activity {

    TextView tvQuestionNumber, tvQuestionDesc;
    Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getActionBar().hide();

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
                    Toast.makeText(GameActivity.this, "CERTO " + "+" + game.getQuestionDifficulty()
                            + " pontos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "ERRADO", Toast.LENGTH_SHORT).show();
                    //Thread.sleep(3 * 1000);
                }
                break;
            case R.id.btnAnswerB:
                if(game.checkAnswer("B")){
                    Toast.makeText(GameActivity.this, "CERTO " + "+" + game.getQuestionDifficulty()
                            + " pontos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "ERRADO", Toast.LENGTH_SHORT).show();
                    //Thread.sleep(3 * 1000);
                }
                break;
            case R.id.btnAnswerC:
                if(game.checkAnswer("C")){
                    Toast.makeText(GameActivity.this, "CERTO " + "+" + game.getQuestionDifficulty()
                            + " pontos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "ERRADO", Toast.LENGTH_SHORT).show();
                    //Thread.sleep(3 * 1000);
                }
                break;
            case R.id.btnAnswerD:
                if(game.checkAnswer("D")){
                    Toast.makeText(GameActivity.this, "CERTO " + "+" + game.getQuestionDifficulty()
                            + " pontos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "ERRADO", Toast.LENGTH_SHORT).show();
                    //Thread.sleep(3 * 1000);
                }
                break;
        }

        proximaPergunta();
    }

    public void proximaPergunta() {
        if(game.checkEnd()) {
            Intent gameResultIntent = new Intent(GameActivity.this, SinglePlayerGameResultActivity.class);
            gameResultIntent.putExtra("result",game.getResult());
            gameResultIntent.putExtra("score",game.getScore());
            gameResultIntent.putExtra("nRightQuestions",game.getnRightQuestions());
            gameResultIntent.putExtra("nWrongQuestions",game.getnWrongQuestions());
            gameResultIntent.putExtra("gameTotalQuestions",game.getnQuestions());
            startActivity(gameResultIntent);
            finish();
        } else {
            actualizaInterface();
        }
    }

    private void actualizaInterface() {
        Question question = game.getNextQuestion();

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

    /*
    @Override
    protected void onResume() {
        super.onResume();
        int questionNumViewValue = game.getCurrentQuestionNum()+1;
        tvQuestionNumber.setText(getResources().getString(R.string.pergunta_numero_text)
                + " " + questionNumViewValue);
        tvQuestionDesc.setText(game.getCurrentQuestion().getQuestionDesc());
        btnAnswerA.setText("A: " + game.getCurrentQuestion().getAnswerA());
        btnAnswerB.setText("B: " + game.getCurrentQuestion().getAnswerB());
        btnAnswerC.setText("C: " + game.getCurrentQuestion().getAnswerC());
        btnAnswerD.setText("D: " + game.getCurrentQuestion().getAnswerD());
    }
    */

    @Override
    public void onBackPressed() {
        /*
         *  The user can't go back when the game is runnig,
         *  he can only give up!
        */
    }
}
