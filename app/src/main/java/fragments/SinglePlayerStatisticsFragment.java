package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import models.MySharedPreferences;
import models.SinglePlayerGameResult;

/**
 * Created by andre
 */

public class SinglePlayerStatisticsFragment extends Fragment implements Updateable {
    private static String GAME_RESULT = "GAME_RESULT";
    private static String GAME_DATE = "GAME_DATE";
    private static String SCORE_ADDED = "SCORE_ADDED";
    private static String N_RIGHT_ANSWERS = "N_RIGHT_ANSWERS";
    private static String P_RIGHT_ANSWERS = "P_RIGHT_ANSWERS";
    private static String N_WRONG_ANSWERS = "N_WRONG_ANSWERS";
    private static String P_WRONG_ANSWERS = "P_WRONG_ANSWERS";
    private static String GAME_N_QUESTIONS = "GAME_N_QUESTIONS";
    private static String GAME_DIFFICULTY = "GAME_DIFFICULTY";

    private View rootView;

    public static ArrayList<HashMap<String, Object>> data;

    public static ListView lvSinglePlayerDetails;

    private void addValuesToAdapter(boolean gameResult, Date gameDate, int gameScore,
                                    int nRightAnswers, double pRightAnswers, int nWrongAnswers,
                                    double pWrongAnswers, int gameNQuestions, String gameDifficulty)
    {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(GAME_RESULT, gameResult);
        hm.put(GAME_DATE, gameDate);
        hm.put(SCORE_ADDED, gameScore);
        hm.put(N_RIGHT_ANSWERS, nRightAnswers);
        hm.put(P_RIGHT_ANSWERS, pRightAnswers);
        hm.put(N_WRONG_ANSWERS, nWrongAnswers);
        hm.put(P_WRONG_ANSWERS, pWrongAnswers);
        hm.put(GAME_N_QUESTIONS, gameNQuestions);
        hm.put(GAME_DIFFICULTY, gameDifficulty);
        data.add(hm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_single_player_statistics, container, false);
        return rootView;
    }

    @Override
    public void update() {
        data = new ArrayList<>();

        if(MySharedPreferences.loadResultTime(getActivity())!=0)
            SinglePlayerGameResult.deleteAllData(getActivity(), new Date().getTime()
                    - (MySharedPreferences.loadResultTime(getActivity()) * Constants.DAY_IN_MS));

        List<SinglePlayerGameResult> singlePlayerGameResultList = SinglePlayerGameResult.loadAllData(getActivity());

        for(SinglePlayerGameResult singlePlayerGameResult : singlePlayerGameResultList) {
            String str_gameDifficulty = "";
            String[] difficulties = getResources().getStringArray(R.array.difficulty);
            switch (singlePlayerGameResult.getGameDifficulty()) {
                case 0: str_gameDifficulty = difficulties[0]; break;
                case 1: str_gameDifficulty = difficulties[1]; break;
                case 2: str_gameDifficulty = difficulties[2]; break;
            }
            addValuesToAdapter(singlePlayerGameResult.getGameResult(), singlePlayerGameResult.getGameDate(),
                    singlePlayerGameResult.getGameScore(), singlePlayerGameResult.getnRightAnswers(),
                    singlePlayerGameResult.getpRightAnswers(), singlePlayerGameResult.getnWrongAnswers(),
                    singlePlayerGameResult.getpWrongAnswers(), singlePlayerGameResult.getGameNQuestions(),
                    str_gameDifficulty);
        }

        lvSinglePlayerDetails = (ListView) rootView.findViewById(R.id.lv_single_player_details);

        lvSinglePlayerDetails.setAdapter(new SinglePlayerDetailsAdapter());

        /*
        lvSinglePlayerDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Clicked on element n." + i, Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    class SinglePlayerDetailsAdapter extends BaseAdapter {

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
            View layout = getActivity().getLayoutInflater().inflate(R.layout.row_single_player_statistic, null);

            LinearLayout ll = (LinearLayout)layout.findViewById(R.id.single_player_details_line);

            boolean gameResult = (boolean) data.get(i).get(GAME_RESULT);
            Date gameDate = (Date) data.get(i).get(GAME_DATE);
            int scoreAdded = (int) data.get(i).get(SCORE_ADDED);
            int nRightAnswers = (int) data.get(i).get(N_RIGHT_ANSWERS);
            double pRightAnswers = (double) data.get(i).get(P_RIGHT_ANSWERS);
            int nWrongAnswers = (int) data.get(i).get(N_WRONG_ANSWERS);
            double pWrongAnswers = (double) data.get(i).get(P_WRONG_ANSWERS);
            int gameNQuestions = (int) data.get(i).get(GAME_N_QUESTIONS);
            String gameDifficulty = (String) data.get(i).get(GAME_DIFFICULTY);

            if(gameResult) {
                ((TextView) layout.findViewById(R.id.tv_game_result)).setText("VITORIA");
                ll.setBackground(getResources().getDrawable(R.drawable.green_white_gradient));
                ((TextView) layout.findViewById(R.id.tv_score_added)).setText("+"+scoreAdded);
            } else {
                ((TextView) layout.findViewById(R.id.tv_game_result)).setText("DERROTA");
                ll.setBackground(getResources().getDrawable(R.drawable.red_white_gradient));
                ((TextView) layout.findViewById(R.id.tv_score_added)).setText(""+scoreAdded);
            }

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy - HH:mm") ;
            String dateFormat = df.format(gameDate).toString();

            ((TextView) layout.findViewById(R.id.tv_game_date))
                    .setText(dateFormat);
            ((TextView) layout.findViewById(R.id.tv_n_right_answers))
                    .setText(String.valueOf(nRightAnswers));
            ((TextView) layout.findViewById(R.id.tv_perc_right_answers))
                    .setText("(" + String.valueOf((int)pRightAnswers) + "%)");
            ((TextView) layout.findViewById(R.id.tv_n_wrong_answers))
                    .setText(String.valueOf(nWrongAnswers));
            ((TextView) layout.findViewById(R.id.tv_perc_wrong_answers))
                    .setText("(" + String.valueOf((int)pWrongAnswers) + "%)");
            ((TextView) layout.findViewById(R.id.tv_game_n_questions))
                    .setText(String.valueOf(gameNQuestions));
            ((TextView) layout.findViewById(R.id.tv_game_difficulty))
                    .setText(gameDifficulty);

            return layout;
        }
    }
}
