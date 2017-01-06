package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import java.util.concurrent.TimeUnit;

import application.InformaticsQuizApp;
import interfaces.Constants;
import models.Game;
import models.MSG;
import models.MySharedPreferences;
import models.MyVibrator;
import models.PlayerData;
import models.Question;
import models.SoundEffect;

/**
 * Created by andre
 */

public class GameActivity extends Activity {
    private InformaticsQuizApp app = null;

    ImageView ivGreenCircle,  ivYellowCircle, ivRedCircle;
    TextView tvQuestionTimer, tvQuestionDesc, tvQuestionProgress;
    ProgressBar pbQuestionProgress;
    Button btnAnswerA, btnAnswerB, btnAnswerC, btnAnswerD;

    private Game game;
    private MyCountDownTimer cdt;

    private boolean giveUp = false;

    private PlayerData playerData = null;
    private int gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_game);

        getActionBar().hide();

        new SoundEffect(this);

        ivGreenCircle = (ImageView) findViewById(R.id.iv_green_circle);
        ivYellowCircle = (ImageView) findViewById(R.id.iv_yellow_circle);
        ivRedCircle = (ImageView) findViewById(R.id.iv_red_circle);
        tvQuestionTimer = (TextView) findViewById(R.id.tv_question_timer);
        tvQuestionProgress = (TextView) findViewById(R.id.tv_question_progress);
        pbQuestionProgress = (ProgressBar) findViewById(R.id.pb_question_progress);
        tvQuestionDesc = (TextView)findViewById(R.id.tv_question_description);
        btnAnswerA = (Button)findViewById(R.id.btn_answer_a);
        btnAnswerB = (Button)findViewById(R.id.btn_answer_b);
        btnAnswerC = (Button)findViewById(R.id.btn_answer_c);
        btnAnswerD = (Button)findViewById(R.id.btn_answer_d);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!((InformaticsQuizApp) getApplication()).isInBackground()) {
            Intent receivedIntent = getIntent();
            if (receivedIntent != null) {
                game = (Game) receivedIntent.getSerializableExtra("game");
                if(game.getTimer())
                    startTimer(game.getQuestionTime());
                if(game.getnPlayers() > 1) {
                    gameMode = Constants.MP_MODE;
                    playerData = PlayerData.loadData(this);
                }
                else
                    gameMode = Constants.SP_MODE;
            }
        } else {
            app = (InformaticsQuizApp) getApplication();
            game = app.getGame();
            if(game.getTimer()) {
                long millis = app.getMyCountDownTimer().getMillis();
                startTimer(millis*1000);
            }
            app.setInBackground(false);
        }
        updateUI();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!game.checkEnd() && !giveUp) {
            app = (InformaticsQuizApp) getApplication();
            app.setGame(game);
            if (game.getTimer()) {
                app.setMyCountDownTimer(cdt);
            }
            app.setInBackground(true);
        }
        stopTimer();
    }

    public void onButtonRespostaClick(View v) {
        stopTimer();

        final Button button = (Button)v;
        boolean answerResult = false;
        switch (v.getId()) {
            case R.id.btn_answer_a:
                answerResult = game.checkAnswer("A");
                break;
            case R.id.btn_answer_b:
                answerResult = game.checkAnswer("B");
                break;
            case R.id.btn_answer_c:
                answerResult = game.checkAnswer("C");
                break;
            case R.id.btn_answer_d:
                answerResult = game.checkAnswer("D");
                break;
        }

        if(gameMode == Constants.MP_MODE) {
            app = (InformaticsQuizApp) getApplication();
            app.getLocalClient().sendMsgToServer(new MSG(Constants.MSG_CODE_ANSWER,
                    getResources().getString(R.string.player_text)+" "+playerData.getName()+" "
                            +getString(R.string.answered_text)+" "+game.getCurrentQuestionNum()+"/"+game.getnQuestions()));
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
            if(game.getTimer())
                startTimer(game.getQuestionTime());
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
        tvQuestionProgress.setText(String.valueOf(game.getCurrentQuestionNum()+1)
                +"/"+String.valueOf(game.getnQuestions()));
        tvQuestionDesc.setText(question.getQuestionDesc());

        btnAnswerA.setText("A: " + question.getAnswerA());
        btnAnswerB.setText("B: " + question.getAnswerB());
        btnAnswerC.setText("C: " + question.getAnswerC());
        btnAnswerD.setText("D: " + question.getAnswerD());
    }

    public void onButtonGiveUp(View view) {
        stopTimer();
        giveUp = true;
        if(game.getGameMode() == Constants.SP_MODE)
            setSinglePlayerGameResult();
        else if(game.getGameMode() == Constants.MP_MODE)
            setMultiPlayerGameResult();
        finish();
    }

    private void startTimer(long millisUntilFinish) {
        cdt = new MyCountDownTimer(millisUntilFinish, Constants.tickTime);
        cdt.start();
    }

    private void stopTimer() {
        if(game.getTimer())
            cdt.cancel();
    }

    private void setSinglePlayerGameResult() {
        app = (InformaticsQuizApp)getApplication();
        app.setInBackground(false);
        Intent gameResultIntent = new Intent(GameActivity.this, SinglePlayerResultActivity.class);
        gameResultIntent.putExtra("game", game);
        startActivity(gameResultIntent);
    }

    private void setMultiPlayerGameResult() {
        app = (InformaticsQuizApp) getApplication();
        app.setInBackground(false);
        app.getLocalClient().sendMsgToServer(new MSG(Constants.MSG_CODE_GAME, game));
    }

    public class MyCountDownTimer extends CountDownTimer {
        private long millis = 0;

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.e("MillisLeft", String.valueOf(millisUntilFinished));
            millis = millisUntilFinished;
            int secondsLeft = (int)(millis / 1000);
            if(secondsLeft <= 10) {
                if(secondsLeft % 2 == 0)
                    tvQuestionTimer.setTextColor(Color.RED);
                else
                    tvQuestionTimer.setTextColor(Color.GRAY);
            } else
                tvQuestionTimer.setTextColor(Color.BLACK);
            String hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis));
            tvQuestionTimer.setText(getString(R.string.time_text)+": "+hms);
            millis = secondsLeft;
            SoundEffect.playTickSound();
        }

        @Override
        public void onFinish() {
            cdt.cancel();
            tvQuestionTimer.setTextColor(Color.RED);
            tvQuestionTimer.setText(getString(R.string.time_text)+": 00");
            Toast.makeText(getApplication(), R.string.times_over_text, Toast.LENGTH_SHORT).show();
            SoundEffect.playWrongAnswerSound();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    game.checkAnswer("");
                    nextQuestion();
                }
            }, Constants.timeToNextQuestion);
        }

        public long getMillis() {
            return millis;
        }
    }
}
