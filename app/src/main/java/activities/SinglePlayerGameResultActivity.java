package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;

import org.xmlpull.v1.XmlPullParser;

public class SinglePlayerGameResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_game_result);

        Intent gameResultIntent = getIntent();
        boolean result = gameResultIntent.getBooleanExtra("result",false);
        int score = gameResultIntent.getIntExtra("score",0);
        int nRightQuestions = gameResultIntent.getIntExtra("nRightQuestions",0);
        int nWrongQuestions = gameResultIntent.getIntExtra("nWrongQuestions",0);
        int gameTotalQuestions = gameResultIntent.getIntExtra("gameTotalQuestions",0);

        View layout = (View) this.getWindow().getDecorView();

        TextView tvGameResult = (TextView) findViewById(R.id.tv_game_result);
        if(result) {
            tvGameResult.setText("PASSAS-TE! Parabéns");
            layout.setBackground(getResources().getDrawable(R.drawable.green_white_gradient));
        } else {
            tvGameResult.setText("CHUMBAS-TE! Tens que te esforçar mais...");
            layout.setBackground(getResources().getDrawable(R.drawable.red_white_gradient));
        }

        TextView tvGameResultDescription = (TextView) findViewById(R.id.tv_game_result_description);
        tvGameResultDescription.setText("Conseguiste obter " + score + " pontos\n"
                + "Acertas-te " + nRightQuestions + "/" + gameTotalQuestions + "\n"
                + "Erras-te " + nWrongQuestions + "/" + gameTotalQuestions);
    }

    public void onButtonGoToInicialMenu(View view) {


        finish();
    }
}
