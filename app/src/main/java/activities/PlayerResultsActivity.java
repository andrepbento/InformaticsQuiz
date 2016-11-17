package activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.andre.informaticsquiz.R;

import adapter.TabsPagerAdapter;
import utils.InformaticsQuizHelper;

public class PlayerResultsActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private Menu menu;
    private String[] tabs = { "Single-Player", "Player", "Multi-Player" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_results);

        actionBar = getActionBar();

        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter((getSupportFragmentManager()));

        viewPager.setAdapter(mAdapter);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
        inflater.inflate(R.menu.player_statistics_menu, menu);
        this.menu = menu;

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int pageNum = viewPager.getCurrentItem();
        if(pageNum == 1) {
            menu.findItem(R.id.item_clear_data).setVisible(false);
        } else {
            menu.findItem(R.id.item_clear_data).setVisible(true);
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
                            dbI.deleteSinglePlayerGameResults();
                            mAdapter.notifyDataSetChanged();
                            break;
                        case 1:
                            //dbI.deleteMultiPlayerGameResults();
                            mAdapter.notifyDataSetChanged();
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
}
