package activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.andre.informaticsquiz.R;

public class HowToPlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        getActionBar().setDisplayHomeAsUpEnabled(true);
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
