package com.byteli.netby.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import com.byteli.netby.NetWork.Hotspot;
import com.byteli.netby.R;
import com.byteli.netby.Socket.Client;
import com.byteli.netby.Socket.Server;

/**
 * Created by byte on 5/18/16.
 */
public class CancelDialog {
    public CancelDialog(Context context, Hotspot hotspot, Server server, Client client) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.setMessage(context.getResources().getString(R.string.info_cancel));
        dialog.show();

        hotspot.closeWifiAp();
        server.disabledServer();
        client.disabledClient();

        dialog.dismiss();
    }
}
