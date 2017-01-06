package activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.example.andre.informaticsquiz.R;

import application.InformaticsQuizApp;
import models.MySharedPreferences;

/**
 * Created by andre
 */

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    private final String REGIST_KEY = "registKey";
    private final String THEME_KEY = "themeKey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MySharedPreferences.loadTheme(this);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.preferences_text);

        ListPreference registListPreference = (ListPreference) findPreference(REGIST_KEY);
        registListPreference.setSummary(registListPreference.getEntry());
        bindPreferenceSummaryToValue(findPreference(REGIST_KEY));
        ListPreference themeListPreference = (ListPreference) findPreference(THEME_KEY);
        themeListPreference.setSummary(themeListPreference.getEntry());
        bindPreferenceSummaryToValue(findPreference(THEME_KEY));
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onMenuItemSelected(featureId, item);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        String stringValue = o.toString();
        if(preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            if(preference.getKey().equals(THEME_KEY)) {
                InformaticsQuizApp app = (InformaticsQuizApp) getApplication();
                app.setPreferencesChanged(true);

                finish();
                startActivity(getIntent());
            }
        }

        return true;
    }
}
