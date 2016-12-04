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
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import adapter.TabsPagerAdapter;
import fragments.SinglePlayerStatisticsFragment;
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

        // Adding Tabs
        for(String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        viewPager.setCurrentItem(1);
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
                Toast.makeText(this, "Implementar o bottão de apagar!", Toast.LENGTH_LONG).show();
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
                            deleteLogData();
                            mAdapter.notifyDataSetChanged();
                            break;
                        case 2:
                            //dbI.deleteMultiPlayerGameResults();
                            mAdapter.notifyDataSetChanged();
                            break;
                    }
                }
                item.setVisible(false);
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

    private void deleteLogData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    InformaticsQuizHelper dbI = new InformaticsQuizHelper(getApplicationContext());
                    dbI.create();
                    if(dbI.open()) {
                        dbI.deleteSinglePlayerGameResults();
                        //dbI.deleteMultiPlayerGameResults();
                        dbI.close();
                    }
                    Toast.makeText(getApplication(), "Player deleted", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE: break;
            }
        }
    };
}
