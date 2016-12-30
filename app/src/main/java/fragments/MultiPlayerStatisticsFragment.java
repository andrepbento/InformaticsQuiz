package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import interfaces.Constants;
import interfaces.Updateable;
import models.MultiPlayerGameResult;
import models.MySharedPreferences;

/**
 * Created by andre
 */

public class MultiPlayerStatisticsFragment extends Fragment implements Updateable {

    private View rootView;

    public static ArrayList<MultiPlayerGameResult> data;

    private void addValuesToAdapter(MultiPlayerGameResult multiPlayerGameResult) {
        data.add(multiPlayerGameResult);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_multi_player_statistics, container, false);
        return  rootView;
    }

    @Override
    public void update() {
        data = new ArrayList<>();

        if(MySharedPreferences.loadResultTime(getActivity())!=0)
            MultiPlayerGameResult.deleteAllData(getActivity(), new Date().getTime()
                    - (MySharedPreferences.loadResultTime(getActivity()) * Constants.DAY_IN_MS));


        List<MultiPlayerGameResult> multiPlayerGameResultList = MultiPlayerGameResult.loadAllData(getActivity());

        if(multiPlayerGameResultList.size() >= 1) {
            for (MultiPlayerGameResult multiPlayerGameResult : multiPlayerGameResultList)
                addValuesToAdapter(multiPlayerGameResult);
            ((TextView) rootView.findViewById(R.id.tv_multi_player_result))
                    .setText(multiPlayerGameResultList.toString());
        }
    }
}
