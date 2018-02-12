package com.zhangteng.xim.bmob.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.io.File;
import java.util.UUID;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class NetUtil {
    public static final int NETWORK_NONE = 0;
    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_MOBILE = 2;
    public static final String NETWORK_NONE_STR = "notconnect";
    public static final String NETWORK_WIFI_STR = "wifi";
    public static final String NETWORK_MOBILE_STR = "mobile";
    public static final String APPLICATION_SUFFIX_STR = ".apk";
    private static final String TAG = "NetUitl";

    public static boolean getNetWorkConnectState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            boolean isAvailable = info.isAvailable();
            if (isAvailable) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static int getNetworkState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);

        // Wifi
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORK_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == State.CONNECTED || state == State.CONNECTING) {
            return NETWORK_MOBILE;
        }
        return NETWORK_NONE;
    }

    public static boolean isNoNetwork(Context context) {
        return (getNetworkState(context) == NETWORK_NONE);
    }

    public static String checkNetworkInfo(Context context) {
        String rs = NETWORK_NONE_STR;
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        //mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

        //wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
            rs = NETWORK_MOBILE_STR;
        }
        if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
            rs = NETWORK_WIFI_STR;
        }

        return rs;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == State.CONNECTED
                            || info[i].getState() == State.CONNECTING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getFileNameFromUrl(String url) {
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf(File.separator) + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf(File.separator) + 1);
        }

        if (filename == null || "".equals(filename.trim())) {
            filename = UUID.randomUUID() + APPLICATION_SUFFIX_STR;
        }
        return filename;
    }
}
