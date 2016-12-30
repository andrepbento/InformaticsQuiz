package activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.andre.informaticsquiz.R;

import models.MySharedPreferences;

/**
 * Created by andre
 */

public class HowToPlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_how_to_play);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.how_to_play_text);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
