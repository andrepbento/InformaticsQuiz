package activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.andre.informaticsquiz.R;

import adapter.TabsPagerAdapter;
import fragments.MultiPlayerStatisticsFragment;
import fragments.SinglePlayerStatisticsFragment;
import models.MultiPlayerGameResult;
import models.MySharedPreferences;
import models.SinglePlayerGameResult;
import utils.InformaticsQuizHelper;

/**
 * Created by andre
 */

public class PlayerResultsActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_player_results);

        tabs[0] = getResources().getString(R.string.single_player_text);
        tabs[1] = getResources().getString(R.string.player_text);
        tabs[2] = getResources().getString(R.string.multi_player_text);

        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.results_text);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter((getSupportFragmentManager()));
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                mAdapter.notifyDataSetChanged();
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        for(String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_player_statistics, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch(viewPager.getCurrentItem()){
            case 0:
                if(SinglePlayerStatisticsFragment.data.isEmpty())
                    menu.findItem(R.id.item_clear_data).setVisible(false);
                else
                    menu.findItem(R.id.item_clear_data).setVisible(true);
                break;
            case 1:
                menu.findItem(R.id.item_clear_data).setVisible(false);
                break;
            case 2:
                if(MultiPlayerStatisticsFragment.data.isEmpty())
                    menu.findItem(R.id.item_clear_data).setVisible(false);
                else
                    menu.findItem(R.id.item_clear_data).setVisible(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_clear_data:
                int pageNum = viewPager.getCurrentItem();
                InformaticsQuizHelper dbI = new InformaticsQuizHelper(this);
                dbI.create();
                if(dbI.open()) {
                    switch(pageNum) {
                        case 0:
                            deleteSinglePlayerData(item);
                            break;
                        case 2:
                            deleteMultiPlayerData(item);
                            break;
                    }
                }
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    private void deleteSinglePlayerData(final MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SinglePlayerGameResult.deleteAllData(getApplicationContext());
                        //MultiPlayerGameResult.deleteAllData();
                        mAdapter.notifyDataSetChanged();
                        item.setVisible(false);
                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }})
                .show();
    }

    private void deleteMultiPlayerData(final MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MultiPlayerGameResult.deleteAllData(getApplicationContext());
                        mAdapter.notifyDataSetChanged();
                        item.setVisible(false);
                    }})
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }})
                .show();
    }
}
