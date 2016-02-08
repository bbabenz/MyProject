package com.example.project.myproject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.format.Formatter;

/**
 * Created by bbabe on 24/1/2559.
 */
public class AndroidID {
    private static AndroidID ourInstance = new AndroidID();
    public static AndroidID getInstance;

    public static AndroidID getInstance() {
        return ourInstance;
    }

    private AndroidID() {
    }

    public static String getandroidid(Context c){
        String android_id = Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }
}
