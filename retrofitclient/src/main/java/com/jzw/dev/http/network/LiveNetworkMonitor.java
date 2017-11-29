package com.jzw.dev.http.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

/**
 * @anthor created by Administrator
 * @date 2017/11/9 0009
 * @change
 * @describe describe
 **/
@SuppressLint("MissingPermission")
public class LiveNetworkMonitor implements NetworkMonitor {
    private final Context mContext;

    public LiveNetworkMonitor(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * 获取网络流量大小 单位kb
     *
     * @return
     */
    public long getNetworkTraffic() {
        long newbyte = TrafficStats.getUidRxBytes((mContext.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (int) (TrafficStats.getTotalRxBytes() / 1024));
        return newbyte;
    }
}
