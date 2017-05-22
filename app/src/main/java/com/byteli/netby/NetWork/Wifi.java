package com.byteli.netby.NetWork;

import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by byte on 1/1/16.
 */
public class Wifi {
    public WifiManager wifiManager;

    public Wifi(WifiManager wifiManager ){
        this.wifiManager = wifiManager;
    }

    public String getServerIp (ConnectivityManager manager) {
        while (true) {
            NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (NetworkInfo.State.CONNECTED == wifi)break;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DhcpInfo info = wifiManager.getDhcpInfo();
        String Ip = Formatter.formatIpAddress(info.serverAddress);
        return Ip;
    }

    public boolean enableNetWork(String ssid){
        WifiConfiguration wifiConfig = setWifiParams(ssid);
        int wcgID = wifiManager.addNetwork(wifiConfig);
        boolean flag = wifiManager.enableNetwork(wcgID, true);

        if(flag){//wait for connection really done if enableNetwork right
            WifiInfo wifiInfo=null;
            while(wifiInfo == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wifiInfo = wifiManager.getConnectionInfo();
            }
        }
        //networkID=wifiInfo.getNetworkId();
        return flag;
    }

    public void checkWifiApState() {
        while (true) {
            Method method = null;
            try {
                method = wifiManager.getClass().getMethod("getWifiApState");
                if ((Integer) method.invoke(wifiManager) == 13) break;
                Thread.sleep(250);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    public WifiConfiguration setWifiParams(String ssid) {
        WifiConfiguration apConfig = new WifiConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            apConfig.SSID = ssid;//in LOLLIPOP do chnage below statement for connecting the wifi
        } else {
            apConfig.SSID = "\"" + ssid + "\"";
        }
        apConfig.preSharedKey = "\""+"*Net By*"+"\"";
        return apConfig;
    }
}
