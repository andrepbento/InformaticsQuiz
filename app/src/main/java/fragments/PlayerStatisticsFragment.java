package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;

import models.PlayerData;

/**
 * Created by andre
 */

public class PlayerStatisticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_player_statistics, container, false);

        PlayerData playerData = PlayerData.loadData(getContext());

        int singlePlayerPontuation = playerData.getSinglePlayerPontuation();
        int multiPlayerPontuation = playerData.getMultiPlayerPontuation();
        int nRightAnswers = playerData.getnRightAnswers();
        int nTotalAnswers = playerData.getTotalAnswers();
        int nWrongAnswers = playerData.getnWrongAnswers();

        ((TextView) rootView.findViewById(R.id.tv_single_player_pontuation))
                .setText(String.valueOf(singlePlayerPontuation));
        ((TextView) rootView.findViewById(R.id.tv_multi_player_pontuation))
                .setText(String.valueOf(multiPlayerPontuation));
        ((TextView) rootView.findViewById(R.id.tv_n_right_answers))
                .setText("Respostas certas"+": "+String.valueOf(nRightAnswers));
        ((TextView) rootView.findViewById(R.id.tv_n_wrong_answers))
                .setText("Respostas erradas"+": "+String.valueOf(nWrongAnswers));
        ((TextView) rootView.findViewById(R.id.tv_n_total_answers))
                .setText("Total de respostas"+": "+String.valueOf(nTotalAnswers));

        return  rootView;
    }
}
