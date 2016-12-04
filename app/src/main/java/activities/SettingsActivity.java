package activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.andre.informaticsquiz.R;

import models.MySharedPreferences;

/**
 * Created by andre
 */

public class SettingsActivity extends Activity {

    //private Button saveButton, cancelButton;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Vibrate = "vibrateKey";
    public static final String LogTime = "logKey";

    MySharedPreferences mySharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class SettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
