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
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import interfaces.Constants;
import models.Game;
import utils.InformaticsQuizHelper;

/**
 * Created by andre
 */

public class CreateGameActivity extends Activity {

    protected InformaticsQuizHelper dbI;

    Spinner difficultySpinner;

    Switch timerSwitch;

    TextView tvnQuestions, tvnPlayers;
    SeekBar seekBarNQuestions, seekBarNPlayers;

    private int gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

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
                    + ": 1");

            seekBarNQuestions = (SeekBar)findViewById(R.id.sb_n_questions);
            seekBarNQuestions.setProgress(1);
            seekBarNQuestions.setMax(Constants.MAX_N_QUESTIONS);
            seekBarNQuestions.setOnSeekBarChangeListener(nQuestionsChangeListener);

            Intent intent = getIntent();
            gameMode = intent.getIntExtra("gameMode", 0);
            String mode = "";
            if(gameMode == Constants.MP_MODE) {
                mode = getResources().getString(R.string.multi_player_text);
                (findViewById(R.id.n_players_piece)).setVisibility(View.VISIBLE);
                tvnPlayers = (TextView)findViewById(R.id.tv_num_players);
                tvnPlayers.setText("Número de jogadores" + ": 2");
                seekBarNPlayers = (SeekBar) findViewById(R.id.sb_n_players);
                seekBarNPlayers.setProgress(2);
                seekBarNPlayers.setMax(Constants.MAX_N_PLAYERS);
                seekBarNPlayers.setOnSeekBarChangeListener(nPlayersChangeListener);
            } else {
                gameMode = Constants.SP_MODE;
                mode = getResources().getString(R.string.single_player_text);
            }
            getActionBar().setTitle(getResources().getString(R.string.game_config_text)+"("
                    +mode+")");
            dbI.close();
        } else {
            Log.e("DB", "Could not open()");
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
            tvnQuestions.setText(getResources().getString(R.string.tv_questions_number_text) + ": " + progress);
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
            tvnPlayers.setText("Número de jogadores" + ": " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public void onButtonComecarClick(View view) {
        if(seekBarNQuestions.getProgress() == 0){
            Toast.makeText(getApplicationContext(), "Falta escolher o número de perguntas...", Toast.LENGTH_SHORT).show();
            return;
        }

        dbI.create();

        if(dbI.open()) {

            String[] difficultyArray = getResources().getStringArray(R.array.difficulty);
            int difficultyId = difficultySpinner.getSelectedItemPosition();
            int n_perguntas = seekBarNQuestions.getProgress();

            Game game = new Game(dbI, difficultyArray, difficultyId, n_perguntas, timerSwitch.isChecked());

            if (gameMode == Constants.SP_MODE) {
                Intent intent_inicia_jogo = new Intent(CreateGameActivity.this, GameActivity.class);
                intent_inicia_jogo.putExtra("game", game);
                startActivity(intent_inicia_jogo);
                finish();
            } else if (gameMode == Constants.MP_MODE) {
                if(seekBarNPlayers.getProgress() < 2 || seekBarNPlayers.getProgress() > 4){
                    Toast.makeText(getApplicationContext(), "Falta escolher o número de jogadores...", Toast.LENGTH_SHORT).show();
                    return;
                }
                SeekBar seekBar = (SeekBar) findViewById(R.id.sb_n_players);
                int nPlayers = seekBar.getProgress();

                Intent intent = new Intent(CreateGameActivity.this, QRCodeActivity.class);
                intent.putExtra("game", game);
                intent.putExtra("nPlayers", nPlayers);
                startActivity(intent);
                finish();
            }
        }
    }
}
