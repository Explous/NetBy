package com.byteli.netby.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;

import com.byteli.netby.NetWork.Wifi;
import com.byteli.netby.R;
import com.byteli.netby.Socket.Client;

/**
 * Created by byte on 5/16/16.
 */
public class ConnectDialog {
    public ConnectDialog (Context context, final ConnectivityManager manager, final Wifi wifi, final Client client, final String ssid){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.app_name);
        dialog.setMessage(context.getResources().getString(R.string.info_join));
        dialog.setCancelable(false);
        dialog.show();

        if (!wifi.wifiManager.isWifiEnabled()) wifi.wifiManager.setWifiEnabled(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!wifi.wifiManager.isWifiEnabled()) try {//wati for eanble WIFI
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                wifi.enableNetWork(ssid);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        client.enabledClient(wifi.getServerIp(manager));
                    }
                }).start();

                while (!client.enabled) try{
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        }).start();

    }
}
