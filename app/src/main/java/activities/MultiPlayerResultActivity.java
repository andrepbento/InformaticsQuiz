package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import application.InformaticsQuizApp;
import models.MultiPlayerGameResult;
import models.MySharedPreferences;
import models.PlayerData;
import models.SoundEffect;

/**
 * Created by andre
 */

public class MultiPlayerResultActivity extends Activity {
    protected String POSITION = "POSITION";
    protected String IMAGE_DATA = "IMAGE_DATA";
    protected String PLAYER_NAME = "PLAYER_NAME";
    protected String PLAYER_PONTUATION = "PLAYER_PONTUATION";

    TextView tvPositionResult, tvGameDate, tvGameNPlayers;

    ArrayList<HashMap<String, Object>> data;

    private void addValuesToAdapter(int position, Bitmap imageData, String playerName, int playerPontuation) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(POSITION,position);
        hm.put(IMAGE_DATA,imageData);
        hm.put(PLAYER_NAME,playerName);
        hm.put(PLAYER_PONTUATION,playerPontuation);
        data.add(hm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_multi_player_result);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.multi_player_game_result_text);

        tvPositionResult = (TextView) findViewById(R.id.tv_position_result);
        tvGameDate = (TextView) findViewById(R.id.tv_game_date);
        tvGameNPlayers = (TextView) findViewById(R.id.tv_game_n_players);

        data = new ArrayList<>();

        if(savedInstanceState==null) {
            Intent multiPlayerGameResultIntent = getIntent();
            long multiPlayerGameResultID
                    = multiPlayerGameResultIntent.getLongExtra("multiPlayerGameResultID", 0);
            updateMultiPlayerResult(MultiPlayerGameResult.loadData(this, multiPlayerGameResultID));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateMultiPlayerResult(MultiPlayerGameResult multiPlayerGameResult) {
        View layout = this.getWindow().getDecorView();

        List<MultiPlayerGameResult.PlayerResult> multiPlayerGameResultList
                = multiPlayerGameResult.getMultiPlayerGameResultTable();
        int position = 1;
        for(MultiPlayerGameResult.PlayerResult playerResult : multiPlayerGameResultList) {
            PlayerData playerData = playerResult.getPlayerData();
            if(playerData.equals(PlayerData.loadData(this))) {
                tvPositionResult.setText(getString(R.string.position_text)+": "+position);
                PlayerData playerDataTmp = PlayerData.loadData(getApplicationContext());
                if(playerResult.getGame().getResult() && position <= multiPlayerGameResultList.size()/2) {
                    playerDataTmp.setMultiPlayerPontuation(playerDataTmp.getMultiPlayerPontuation()
                            +playerResult.getGame().getScore());
                    layout.setBackground(getResources().getDrawable(R.drawable.green_white_gradient));
                    SoundEffect.playWinGameSound();
                } else {
                    int looseScore = playerResult.getGame().getTotalScore()/2
                            - playerResult.getGame().getScore();
                    playerDataTmp.setMultiPlayerPontuation(playerDataTmp.getMultiPlayerPontuation()
                            -looseScore);
                    layout.setBackground(getResources().getDrawable(R.drawable.red_white_gradient));
                    SoundEffect.playLoseGameSound();
                }
                playerDataTmp.setnRightAnswers(playerDataTmp.getnRightAnswers()
                        +playerResult.getGame().getnRightQuestions());
                playerDataTmp.setnWrongAnswers(playerDataTmp.getnWrongAnswers()
                        +playerResult.getGame().getnWrongQuestions());
                playerDataTmp.setTotalAnswers(playerDataTmp.getTotalAnswers()
                        +playerResult.getGame().getnQuestions());
                playerDataTmp.saveData(getApplicationContext());
            }

            addValuesToAdapter(position, playerData.getPhoto(), playerData.getName(), playerResult.getGame().getScore());

            position++;
        }

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy\nHH:mm") ;
        String dateFormat = df.format(new Date(multiPlayerGameResult.getMultiPlayerGameResultID()));

        tvGameDate.setText(dateFormat);
        tvGameNPlayers.setText(multiPlayerGameResultList.size()+" "+getString(R.string.players_text));

        ListView listViewMultiPlayerResults = (ListView) findViewById(R.id.lv_multi_player_results);
        listViewMultiPlayerResults.setAdapter(new MultiPlayerResultListAdapter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAllComunications();
    }

    private void stopAllComunications() {
        InformaticsQuizApp app = (InformaticsQuizApp) getApplication();
        if(app.getLocalClient()!=null) {
            app.getLocalClient().stopClient();
            app.setLocalClient(null);
        }
        if(app.getLocalServer()!=null){
            app.getLocalServer().stopServer();
            app.setLocalServer(null);
        }
    }

    class MultiPlayerResultListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View layout = getLayoutInflater().inflate(R.layout.row_multi_player_statistic, null);

            Integer position = (Integer) data.get(i).get(POSITION);
            Bitmap playerImage = (Bitmap) data.get(i).get(IMAGE_DATA);
            String playerName = (String) data.get(i).get(PLAYER_NAME);
            Integer playerPontuation = (Integer) data.get(i).get(PLAYER_PONTUATION);

            ((TextView) layout.findViewById(R.id.tv_player_position))
                    .setText(position.toString());
            ((ImageView) layout.findViewById(R.id.iv_player_image))
                    .setImageBitmap(Bitmap.createScaledBitmap(playerImage, 200, 200, false));
            ((TextView) layout.findViewById(R.id.tv_player_name))
                    .setText(playerName);
            ((TextView) layout.findViewById(R.id.tv_player_pontuation))
                    .setText(String.valueOf(playerPontuation));

            return layout;
        }
    }
}
