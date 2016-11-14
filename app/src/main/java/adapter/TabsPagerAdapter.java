package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fragments.MultiPlayerStatisticsFragment;
import fragments.PlayerStatisticsFragment;
import fragments.SinglePlayerStatisticsFragment;

/**
 * Created by andre on 10/11/2016.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return new SinglePlayerStatisticsFragment();
            case 1:
                return new PlayerStatisticsFragment();
            case 2:
                return new MultiPlayerStatisticsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        if(object instanceof SinglePlayerStatisticsFragment) {
            SinglePlayerStatisticsFragment f = (SinglePlayerStatisticsFragment) object;
            f.update();
        } else if (object instanceof PlayerStatisticsFragment) {

        } else if(object instanceof MultiPlayerStatisticsFragment) {

        }
        return super.getItemPosition(object);
    }
}
