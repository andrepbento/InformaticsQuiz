package fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import interfaces.Constants;
import interfaces.Updateable;
import models.MultiPlayerGameResult;
import models.MySharedPreferences;
import models.PlayerData;

/**
 * Created by andre
 */

public class MultiPlayerStatisticsFragment extends Fragment implements Updateable {
    protected String MULTI_PLAYER_GAME_RESULT = "MULTI_PLAYER_GAME_RESULT";
    protected String POSITION = "POSITION";
    protected String GAME_STATE = "GAME_STATE";
    protected String IMAGE_DATA = "IMAGE_DATA";
    protected String PLAYER_NAME = "PLAYER_NAME";
    protected String PLAYER_PONTUATION = "PLAYER_PONTUATION";

    private View rootView;

    public static ArrayList<HashMap<String, Object>> firstListViewData;
    public static ArrayList<HashMap<String, Object>> secondListViewData;

    public static ListView firstListView;
    public static ListView secondListView;

    private void addValuesToFirstListViewAdapter(MultiPlayerGameResult multiPlayerGameResult, int position,
                                     boolean gameState) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(MULTI_PLAYER_GAME_RESULT,multiPlayerGameResult);
        hm.put(POSITION,position);
        hm.put(GAME_STATE,gameState);
        firstListViewData.add(hm);
    }

    private void addValuesToSecondListViewAdapter(int position, Bitmap imageData, String playerName, int playerPontuation) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(POSITION,position);
        hm.put(IMAGE_DATA,imageData);
        hm.put(PLAYER_NAME,playerName);
        hm.put(PLAYER_PONTUATION,playerPontuation);
        secondListViewData.add(hm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_multi_player_statistics, container, false);
        return  rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    @Override
    public void update() {
        firstListViewData = new ArrayList<>();

        if(MySharedPreferences.loadResultTime(getActivity())!=0)
            MultiPlayerGameResult.deleteAllData(getActivity(), new Date().getTime()
                    - (MySharedPreferences.loadResultTime(getActivity()) * Constants.DAY_IN_MS));


        List<MultiPlayerGameResult> multiPlayerGameResultList = MultiPlayerGameResult.loadAllData(getActivity());

        int position = 1;
        boolean gameState = false;
        for(MultiPlayerGameResult multiPlayerGameResult : multiPlayerGameResultList) {
            for (MultiPlayerGameResult.PlayerResult playerResult : multiPlayerGameResult.getMultiPlayerGameResultTable()) {
                PlayerData playerData = playerResult.getPlayerData();
                if (playerData.equals(PlayerData.loadData(getActivity()))) {
                    if (playerResult.getGame().getResult() && position <= multiPlayerGameResult.getMultiPlayerGameResultTable().size() / 2 )
                        gameState = true;
                    break;
                } else
                    position++;
            }
            addValuesToFirstListViewAdapter(multiPlayerGameResult, position, gameState);
        }

        firstListView = (ListView) getActivity().findViewById(R.id.lv_multi_player_details);
        firstListView.setAdapter(new FirstListViewAdapter());
    }

    class FirstListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return firstListViewData.size();
        }

        @Override
        public HashMap<String, Object> getItem(int i) {
            return firstListViewData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View layout = getActivity().getLayoutInflater().inflate(R.layout.fragment_multi_player_list_row, null);

            secondListViewData = new ArrayList<>();

            Integer position = (Integer) firstListViewData.get(i).get(POSITION);
            MultiPlayerGameResult multiPlayerGameResult = (MultiPlayerGameResult) firstListViewData.get(i).get(MULTI_PLAYER_GAME_RESULT);
            Boolean gameState = (Boolean) firstListViewData.get(i).get(GAME_STATE);

            if(gameState)
                layout.setBackground(getResources().getDrawable(R.drawable.green_white_gradient));
            else
                layout.setBackground(getResources().getDrawable(R.drawable.red_white_gradient));

            ((TextView) layout.findViewById(R.id.tv_position_result))
                    .setText(getResources().getString(R.string.position_text)+" "+String.valueOf(position));

            int p = 0;
            for(MultiPlayerGameResult.PlayerResult playerResult : multiPlayerGameResult.getMultiPlayerGameResultTable()) {
                p++;
                addValuesToSecondListViewAdapter(p, playerResult.getPlayerData().getPhoto(),
                        playerResult.getPlayerData().getName(), playerResult.getGame().getScore());
            }

            secondListView = (ListView) layout.findViewById(R.id.lv_multi_player_results);
            secondListView.setAdapter(new SecondListViewAdapter());

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy\nHH:mm");
            String dateFormat = df.format(new Date(multiPlayerGameResult.getMultiPlayerGameResultID()));
            ((TextView) layout.findViewById(R.id.tv_game_date))
                    .setText(dateFormat);
            ((TextView) layout.findViewById(R.id.tv_game_n_players))
                    .setText(multiPlayerGameResult.getMultiPlayerGameResultTable().size()
                            +" "+getString(R.string.players_text));

            //secondListView.getAdapter().notifyAll();

            return layout;
        }
    }

    class SecondListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return secondListViewData.size();
        }

        @Override
        public HashMap<String, Object> getItem(int i) {
            return secondListViewData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View layout = getActivity().getLayoutInflater().inflate(R.layout.row_multi_player_statistic, null);

            Integer position = (Integer) secondListViewData.get(i).get(POSITION);
            Bitmap playerPhoto = (Bitmap) secondListViewData.get(i).get(IMAGE_DATA);
            String playerName = (String) secondListViewData.get(i).get(PLAYER_NAME);
            Integer playerPontuation = (Integer) secondListViewData.get(i).get(PLAYER_PONTUATION);

            ((TextView) layout.findViewById(R.id.tv_player_position))
                    .setText(String.valueOf(position));
            ((ImageView) layout.findViewById(R.id.iv_player_image))
                    .setImageBitmap(Bitmap.createScaledBitmap(playerPhoto, 150, 150, false));
            ((TextView) layout.findViewById(R.id.tv_player_name))
                    .setText(playerName);
            ((TextView) layout.findViewById(R.id.tv_player_pontuation))
                    .setText(String.valueOf(playerPontuation));

            return layout;
        }
    }
}
