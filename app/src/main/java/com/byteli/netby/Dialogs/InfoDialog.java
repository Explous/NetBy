package com.byteli.netby.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.byteli.netby.R;

/**
 * Created by byte on 6/14/16.
 */
public class InfoDialog {
    public InfoDialog(Context context, String info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(R.string.app_name).setMessage(info)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }
}
