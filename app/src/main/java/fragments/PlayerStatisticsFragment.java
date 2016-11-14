package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andre.informaticsquiz.R;

/**
 * Created by andre on 14/11/2016.
 */

public class PlayerStatisticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_player_statistics, container, false);

        return  rootView;
    }
}
