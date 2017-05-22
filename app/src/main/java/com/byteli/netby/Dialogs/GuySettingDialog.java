package com.byteli.netby.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.byteli.netby.R;
import com.byteli.netby.Variables;

/**
 * Created by byte on 5/18/16.
 */
public class GuySettingDialog{
    public GuySettingDialog(Context context, AppCompatActivity appCompatActivity, final Variables variables, final Handler handler) {
        View view = appCompatActivity.getLayoutInflater().inflate(R.layout.dialog_guy_setting, null);
        final EditText editID = (EditText)view.findViewById(R.id.edit_id);
        editID.setText(variables.guyID);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle(R.string.app_name).setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String edit = editID.getText().toString();
                if (edit.length() != 0 && !edit.equals(variables.guyID)){
                    variables.guyID = edit;
                    handler.sendMessage(handler.obtainMessage(1, "notify_guy"));
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }
}