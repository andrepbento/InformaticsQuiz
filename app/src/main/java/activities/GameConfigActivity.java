package activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import helper.InformaticsQuizHelper;

public class GameConfigActivity extends Activity {

    protected InformaticsQuizHelper dbI;

    ActionBar actionBar;

    Spinner spinner;

    TextView tvnQuestions;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        dbI = new InformaticsQuizHelper(this);

        dbI.create();

        if(!dbI.open()){
            Toast.makeText(getApplicationContext(), "Erro BD", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            actionBar = getActionBar();
            actionBar.setHomeButtonEnabled(true);

            spinner = (Spinner) findViewById(R.id.spinner_dificuldade);
            // Criar um ArrayAdapter usando um String[] e um default Spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.difficulty, android.R.layout.simple_spinner_item);
            // Especificar o layout a user quando a lista de escolhas aparecer
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Aplicar o adapter ao Spinner
            spinner.setAdapter(adapter);

            int nQuestions = dbI.getAllQuestions().size();

            tvnQuestions = (TextView)findViewById(R.id.tv_num_perguntas);
            tvnQuestions.setText(getResources().getString(R.string.numero_de_perguntas_text_view)
                    + "1");

            seekBar = (SeekBar)findViewById(R.id.seek_bar_n_perguntas);
            seekBar.setProgress(1);
            seekBar.setMax(15);
            seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
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

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tvnQuestions.setText(getResources().getString(R.string.numero_de_perguntas_text_view) + " " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public void onButtonComecarClick(View view) {
        // Obter difficulty e nQuestions
        if(seekBar.getProgress() == 0){
            Toast.makeText(getApplicationContext(), "Falta escolher o número de perguntas...", Toast.LENGTH_SHORT).show();
        } else {
            String [] dificuldade = getResources().getStringArray(R.array.difficulty);
            int n_perguntas = seekBar.getProgress();

            Toast.makeText(getApplicationContext(), "Iniciar o game com dif:"
                            + dificuldade[spinner.getSelectedItemPosition()] + ", n_per:"
                            + n_perguntas, Toast.LENGTH_SHORT).show();

            Intent intent_inicia_jogo = new Intent(GameConfigActivity.this, GameActivity.class);
            intent_inicia_jogo.putExtra("difficulty",dificuldade[spinner.getSelectedItemPosition()]);
            intent_inicia_jogo.putExtra("nQuestions",n_perguntas);
            startActivity(intent_inicia_jogo);
            finish();
        }
    }
}
