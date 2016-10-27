package com.donting.sunshine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by yanhuiwang on 16/10/23.
 */

public class NetWorkConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process process = runtime.exec("ping -c 1 -w 100 www.baidu.com");
                process.waitFor();
                int exitValue = process.exitValue();
                Log.d(NetWorkConnectionChangeReceiver.class.getSimpleName(), "exitValue = " + exitValue);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
