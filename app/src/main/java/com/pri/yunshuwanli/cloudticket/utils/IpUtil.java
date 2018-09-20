package com.pri.yunshuwanli.cloudticket.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class IpUtil {
    private String getNetIp(Context ctx){
        try {
            NetworkInfo info = ((ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                URL url = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
                URLConnection conn = url.openConnection();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(),
                                "utf-8"));
                String line = null;
                StringBuffer result = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                String rString = result.toString().replace("var returnCitySN = ", "");
                rString = rString.substring(0, rString.length() - 1);
                JSONObject jsonObject = new JSONObject(rString);
                return jsonObject.getString("cip");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";
    }

    private String getWifiIp(Context ctx){
        try {
            NetworkInfo info = ((ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
                WifiInfo infowifi = wifiManager.getConnectionInfo();
                String ip = intIP2StringIP(infowifi.getIpAddress());
                return ip;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
