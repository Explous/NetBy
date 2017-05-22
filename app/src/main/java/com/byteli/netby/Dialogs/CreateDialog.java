package com.byteli.netby.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.byteli.netby.NetWork.Hotspot;
import com.byteli.netby.R;
import com.byteli.netby.Socket.Client;
import com.byteli.netby.Socket.Server;

/**
 * Created by byte on 5/15/16.
 */
public class CreateDialog {
    public CreateDialog(Context context, final Handler handler, Hotspot hotspot, final Server server, final Client client, String ssid){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(R.string.app_name);
        dialog.setCancelable(false);
        dialog.setMessage(context.getResources().getString(R.string.info_create));
        dialog.show();

        hotspot.enabledWifiAp(ssid);

        new Thread(new Runnable() {
            @Override
            public void run() {
                server.enabledServer();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!server.enabled) try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                client.isHost = true;
                if (client.enabledClient("192.168.43.1"))System.out.println("Connect to the Server success!");
                else System.out.println("Connect to the Server error!");
                handler.sendMessage(handler.obtainMessage(1, "changeCreate"));
                dialog.dismiss();
            }
        }).start();
    }
}
