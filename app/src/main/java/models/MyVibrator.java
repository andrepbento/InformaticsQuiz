package models;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by andre
 */

public class MyVibrator {
    private Context mContext;
    private Vibrator mVibrator;

    public MyVibrator(Context context) {
        mContext = context;
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate(long length) {
        mVibrator.vibrate(length);
    }
}
