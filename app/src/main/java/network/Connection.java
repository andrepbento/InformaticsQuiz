package network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by andre
 */

public final class Connection {

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)	context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected())
            return false;
        return true;
    }
}
