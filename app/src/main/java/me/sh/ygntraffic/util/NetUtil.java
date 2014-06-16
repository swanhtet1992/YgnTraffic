package me.sh.ygntraffic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author yelinaung
 */
public class NetUtil {
  // Check the device is connected to the internet
  public static boolean isOnline(Context c) {
    NetworkInfo netInfo = null;
    try {
      ConnectivityManager cm =
          (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
      netInfo = cm.getActiveNetworkInfo();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }
}
