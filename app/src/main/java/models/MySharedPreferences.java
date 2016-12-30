package models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.andre.informaticsquiz.R;

/**
 * Created by andre
 */

public class MySharedPreferences {
    public boolean vibrate;
    private String VIBRATE = "VIBRATE";

    public int resultTime;
    private static final String RESULT_TIME = "resultKey";
    private static final int ONE_DAY = 0;
    private static final int FIVE_DAYS = 1;
    private static final int FIFHTHEEN_DAYS = 2;
    private static final int ONE_MONTH = 3;
    private static final int THREE_MONTHS = 4;

    private static final String THEME = "themeKey";
    private static final int BLACK_THEME = 0;
    private static final int RED_THEME = 1;
    private static final int BLUE_THEME = 2;
    private static final int PINK_THEME = 3;
    private static final int YELLOW_THEME = 4;

    public static int loadResultTime(Context context) {
        SharedPreferences tmpSharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(context);

        String storedTime = tmpSharedPreferences.getString(RESULT_TIME, "0");
        switch (Integer.parseInt(storedTime)) {
            case ONE_DAY:
                return 1;
            case FIVE_DAYS:
                return 5;
            case FIFHTHEEN_DAYS:
                return 15;
            case ONE_MONTH:
                return 30;
            case THREE_MONTHS:
                return 90;
        }
        return 0;
    }

    public static void loadTheme(Activity activity) {
        SharedPreferences tmpSharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(activity);

        String storedTheme = tmpSharedPreferences.getString(THEME, "0");
        switch(Integer.parseInt(storedTheme)) {
            case BLACK_THEME:
                activity.setTheme(R.style.AppThemeBlack);
                break;
            case RED_THEME:
                activity.setTheme(R.style.AppThemeRed);
                break;
            case BLUE_THEME:
                activity.setTheme(R.style.AppThemeBlue);
                break;
            case PINK_THEME:
                activity.setTheme(R.style.AppThemePink);
                break;
            case YELLOW_THEME:
                activity.setTheme(R.style.AppThemeYellow);
        }
    }
}
