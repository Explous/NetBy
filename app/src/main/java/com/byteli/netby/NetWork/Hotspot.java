package com.byteli.netby.NetWork;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by byte on 1/1/16.
 */
public class Hotspot {
    WifiManager wifiManager;

    public Hotspot(WifiManager wifiManager){
        this.wifiManager = wifiManager;
    }

    public boolean isWifiApEnable(){
        Method method = null;
        try {
            method = wifiManager.getClass().getMethod("getWifiApState");
            if((Integer)method.invoke(wifiManager)==13)return true;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean enabledWifiAp(String apName){
        wifiManager.setWifiEnabled(false);
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            apConfig.SSID = apName;
            apConfig.preSharedKey = "*Net By*";
            //配置热点的密码
            apConfig.hiddenSSID = true;
            apConfig.status = WifiConfiguration.Status.ENABLED;
            apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            apConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            apConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            apConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            apConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            //enable the WifiAp
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            return (boolean) method.invoke(wifiManager, apConfig, true);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public void closeWifiAp(){
        try {
            WifiConfiguration apConfig = new WifiConfiguration();
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            method.invoke(wifiManager, apConfig, false);
        } catch (Exception e) {
        }
    }
}
