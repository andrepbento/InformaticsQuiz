package activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.andre.informaticsquiz.R;

import interfaces.Constants;
import models.MySharedPreferences;

/**
 * Created by andre
 */

public class AboutTheAppActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_about_the_app);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.about_the_app_text);

        (findViewById(R.id.iv_facebook_logo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOnUrl(Constants.URL_FACEBOOK_PROGRAMER_PROFILE);
            }
        });

        (findViewById(R.id.iv_play_store_logo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOnUrl(Constants.URL_GITHUB);
            }
        });

        (findViewById(R.id.iv_isec_logo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOnUrl(Constants.URL_ISEC);
            }
        });
    }

    private void searchOnUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
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
