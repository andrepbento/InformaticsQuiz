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
 * Created by andre on 14/11/2016.
 */

public class PlayerStatisticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_player_statistics, container, false);

        PlayerData playerData = PlayerData.loadData(getContext());

        int playerPontuation = playerData.getPontuation();

        ((TextView) rootView.findViewById(R.id.tv_single_player_pontuation))
                .setText(String.valueOf(playerPontuation));

        return  rootView;
    }
}
