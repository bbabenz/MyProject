package com.example.project.myproject;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class IP {
    public static IP getInstance;
    private int ip;
    private static IP ourInstance = new IP();

    public static IP getInstance() {
        return ourInstance;
    }

    private IP() {
    }


    public static String getIP(Context c){
        WifiManager wifiMgr = (WifiManager) c.getSystemService(c.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        final String address = ipAddress.substring(0,12)+"1";
        return address;
        }


}

