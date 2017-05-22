package com.byteli.netby.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.byteli.netby.GlobalVariable;
import com.byteli.netby.KeyNetBy;
import com.byteli.netby.R;
import com.byteli.netby.Socket.Client;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by byte on 6/1/16.
 */
public class InviteDialog {
    InviteSimpleAdapter inviteSimpleAdapter;
    String multisInfo = "";
    int count = 0;
    int most = 0;
    Client client;
    public InviteDialog(AppCompatActivity appCompatActivity, int most, final boolean[] multis, final List<Map<String, Object>> guys, final String packageName) {
        this.most = most;
        count = 1;
        client = ((GlobalVariable)appCompatActivity.getApplication()).getClient();

        checkMultis(multis);

        //final ListView invite = new ListView(appCompatActivity);
        View view = appCompatActivity.getLayoutInflater().inflate(R.layout.dialog_invite, null);
        final ListView invite = (ListView)view.findViewById(R.id.list);
        TextView info = (TextView)view.findViewById(R.id.info);
        info.setText(multisInfo);
        inviteSimpleAdapter = new InviteSimpleAdapter(appCompatActivity, guys, R.layout.listitem_invite, new String[]{"Label"}, new int[]{R.id.checkbox});
        invite.setAdapter(inviteSimpleAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity).setTitle("Invite").setView(view).setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String beinvited = "" + client.myID;//the message content the guys be invited
                        if (count != 1 && multis[count - 1] == true) {//check the choose wether support by the app
                            for (int temp = 0; temp < invite.getChildCount(); temp++)
                                if (((CheckBox) invite.getChildAt(temp).findViewById(R.id.checkbox)).isChecked())
                                    beinvited = beinvited + guys.get(temp).get("ID");
                            try {
                                client.outputStream.writeUTF("" + KeyNetBy.HEAD_APP + KeyNetBy.APP_INVITE + packageName + KeyNetBy.MARK + beinvited);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        if (multis[0] == true)
            builder.setNeutralButton(R.string.single, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        builder.create().show();
    }

    private void checkMultis(boolean[] multis) {
        if (multis[0] == true) multisInfo = multisInfo + "单人模式\n";
        if (multis[1] == true) multisInfo = multisInfo + "双人模式\n";
        if (multis[2] == true) multisInfo = multisInfo + "三人模式\n";
        if (multis[3] == true) multisInfo = multisInfo + "四人模式\n";
        if (multis[4] == true) multisInfo = "无人数限制\n";
    }

    public class InviteSimpleAdapter extends SimpleAdapter {
        private LayoutInflater mInflater;
        List<Map<String, Object>> guys;
        AppCompatActivity appCompatActivity;
        int layout;
        public InviteSimpleAdapter(AppCompatActivity appCompatActivity, List<Map<String, Object>> data, int layout, String[] from, int[] to) {
            super(appCompatActivity, data, layout, from, to);
            this.layout = layout;
            this.guys = data;
            this.appCompatActivity = appCompatActivity;
            mInflater = (LayoutInflater)appCompatActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convert, ViewGroup parent) {
            View convertView = mInflater.inflate(layout, null);
            Map<String, Object> item = guys.get(position);
            CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
            checkBox.setText(item.get("Label").toString());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        if (count + 1 <= most)count ++;
                        else buttonView.setChecked(false);
                    else count--;
                }
            });
            return convertView;
        }
    }
}
