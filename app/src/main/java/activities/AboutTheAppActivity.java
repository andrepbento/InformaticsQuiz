package activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.andre.informaticsquiz.R;

/**
 * Created by andre
 */

public class AboutTheAppActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_the_app);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.about_the_app_text);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
