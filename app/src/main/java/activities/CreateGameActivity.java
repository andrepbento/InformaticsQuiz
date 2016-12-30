package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;

import interfaces.Constants;
import models.Game;
import models.MySharedPreferences;
import utils.InformaticsQuizHelper;

/**
 * Created by andre
 */

public class CreateGameActivity extends Activity {
    protected InformaticsQuizHelper dbI;

    TextView tvnQuestions, tvnPlayers;
    SeekBar seekBarNQuestions, seekBarNPlayers;
    Spinner difficultySpinner;
    Switch timerSwitch;

    private int gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_create_game);

        setTheme(R.style.AppThemeRed);

        dbI = new InformaticsQuizHelper(this);
        dbI.create();
        if(dbI.open()) {
            getActionBar().setDisplayHomeAsUpEnabled(true);

            difficultySpinner = (Spinner) findViewById(R.id.spinner_dificuldade);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.difficulty, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            difficultySpinner.setAdapter(adapter);

            timerSwitch = (Switch) findViewById(R.id.sw_timer);

            tvnQuestions = (TextView)findViewById(R.id.tv_num_perguntas);
            tvnQuestions.setText(getResources().getString(R.string.tv_questions_number_text)
                    + ": 10");

            seekBarNQuestions = (SeekBar)findViewById(R.id.sb_n_questions);
            seekBarNQuestions.setProgress(9);
            seekBarNQuestions.setMax(Constants.MAX_N_QUESTIONS-1);
            seekBarNQuestions.setOnSeekBarChangeListener(nQuestionsChangeListener);

            Intent intent = getIntent();
            gameMode = intent.getIntExtra("gameMode", 0);
            if(gameMode == Constants.SP_MODE) {
                gameMode = Constants.SP_MODE;
                getActionBar().setTitle(getResources().getString(R.string.game_config_text)+" "
                        +getResources().getString(R.string.single_player_text));
            } else if(gameMode == Constants.MP_MODE) {
                gameMode = Constants.MP_MODE;
                getActionBar().setTitle(getResources().getString(R.string.game_config_text)+" "
                        +getResources().getString(R.string.multi_player_text));
                (findViewById(R.id.n_players_piece)).setVisibility(View.VISIBLE);
                seekBarNPlayers = (SeekBar) findViewById(R.id.sb_n_players);
                seekBarNPlayers.setProgress(0);
                seekBarNPlayers.setMax(Constants.MAX_N_PLAYERS-2);
                seekBarNPlayers.setOnSeekBarChangeListener(nPlayersChangeListener);
                tvnPlayers = (TextView)findViewById(R.id.tv_num_players);
                tvnPlayers.setText(getString(R.string.number_of_players_text)+": "
                        +(seekBarNPlayers.getProgress()+2));
            }

            dbI.close();
        } else {
            Log.e("CreateGameActivity", "!open()");
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    SeekBar.OnSeekBarChangeListener nQuestionsChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tvnQuestions.setText(getResources().getString(R.string.tv_questions_number_text)+": "+(progress+1));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    SeekBar.OnSeekBarChangeListener nPlayersChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tvnPlayers.setText(getString(R.string.number_of_players_text)+": "+(progress+2));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public void onButtonComecarClick(View view) {
        dbI.create();
        if(dbI.open()) {
            String[] difficultyArray = getResources().getStringArray(R.array.difficulty);
            int difficultyId = difficultySpinner.getSelectedItemPosition();
            int nQuestions = seekBarNQuestions.getProgress()+1;

            Game game = new Game(dbI, difficultyArray, difficultyId, nQuestions, timerSwitch.isChecked());

            Intent intent = null;
            if (gameMode == Constants.SP_MODE) {
                intent = new Intent(CreateGameActivity.this, GameActivity.class);
                game.setGameMode(Constants.SP_MODE);
                game.setnPlayers(1);
            } else if (gameMode == Constants.MP_MODE) {
                intent = new Intent(CreateGameActivity.this, QRCodeActivity.class);
                game.setGameMode(Constants.MP_MODE);
                game.setnPlayers(seekBarNPlayers.getProgress()+2);
            }
            intent.putExtra("game", game);
            startActivity(intent);
            finish();
        }
    }
}
