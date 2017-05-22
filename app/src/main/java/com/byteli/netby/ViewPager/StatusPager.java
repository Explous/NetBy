package com.byteli.netby.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byteli.netby.Dialogs.CancelDialog;
import com.byteli.netby.Dialogs.CreateDialog;
import com.byteli.netby.NetWork.Hotspot;
import com.byteli.netby.Socket.Client;
import com.byteli.netby.Socket.Server;
import com.google.zxing.client.android.QRCodeUtil;
import com.byteli.netby.R;
import com.byteli.netby.Widgets.Widgets;
import com.google.zxing.client.android.CaptureActivity;

import java.io.File;
import java.util.Calendar;

/**
 * Created by byte on 5/8/16.
 */
public class StatusPager {
    AppCompatActivity appCompatActivity;
    ImageView qrView;
    String filePath;
    Context context;
    boolean isCreate = false;
    boolean isJoin = false;

    ImageView createIcon;
    ImageView joinIcon;
    TextView createTitle;
    TextView joinTitle;
    LinearLayout create;
    LinearLayout join;

    String ssid = null;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if (msg.obj.toString().equals("changeCreate"))changeCreate();//When the qr scan success,update the create button,or do nothing
        }
    };

    public StatusPager(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    public View deploy(final Context context, final Widgets widgets, final Hotspot hotspot, final Server server, final Client client){
        this.context = context;
        final View view = appCompatActivity.getLayoutInflater().inflate(R.layout.viewpager_status, null);
        create = (LinearLayout) view.findViewById(R.id.butCreate);
        join = (LinearLayout) view.findViewById(R.id.butJoin);
        qrView = (ImageView)view.findViewById(R.id.qr);
        createIcon = (ImageView)view.findViewById(R.id.create_icon);
        createTitle = (TextView)view.findViewById(R.id.create_title);
        joinIcon = (ImageView)view.findViewById(R.id.join_icon);
        joinTitle = (TextView)view.findViewById(R.id.join_title);

        filePath = getFileRoot(context) + File.separator
                + "qr_view" + ".jpg";

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreate) {
                    new CancelDialog(context, hotspot, server, client);
                    isCreate = false;
                    join.setVisibility(View.VISIBLE);

                    clear();
                    createIcon.setImageResource(R.mipmap.icon_create);
                    createTitle.setText(context.getResources().getString(R.string.create));
                }
                else {
                    Calendar c = Calendar.getInstance();
                    ssid = "guy" + '.' + c.get(Calendar.HOUR) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND);
                    new CreateDialog(context, handler, hotspot, server, client, ssid);
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isJoin) {
                    isJoin = false;
                    create.setVisibility(View.VISIBLE);
                    joinIcon.setImageResource(R.mipmap.icon_join);
                    joinTitle.setText(context.getResources().getString(R.string.join));

                    clear();
                    client.disabledClient();
                } else {
                    Intent intent = new Intent(appCompatActivity, CaptureActivity.class);
                    appCompatActivity.startActivityForResult(intent, 1);
                }
            }
        });

        return view;
    }

    private void changeCreate() {
        isCreate = true;
        join.setVisibility(View.INVISIBLE);

        createQRImage(ssid);
        createIcon.setImageResource(R.mipmap.icon_cancel);
        createTitle.setText(context.getResources().getString(R.string.cancel));
    }

    public void changeJoin() {
        isJoin = true;
        create.setVisibility(View.INVISIBLE);
        joinIcon.setImageResource(R.mipmap.icon_cancel);
        joinTitle.setText(context.getResources().getString(R.string.quit));
    }

    public void createQRImage(String ssid){
        DisplayMetrics dm = new DisplayMetrics();
        appCompatActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        boolean success = QRCodeUtil.createQRImage(ssid, dm.widthPixels / 2, dm.widthPixels / 2, null, filePath);
        if (success) qrView.setImageBitmap(BitmapFactory.decodeFile(filePath));
        else System.out.println("Create qr error!");
    }

    private String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath();
    }

    public void clear() {
        qrView.setImageBitmap(null);
        if (isJoin) {//those code for the abnormal disconnect connection
            create.setVisibility(View.VISIBLE);
            joinIcon.setImageResource(R.mipmap.icon_join);
            joinTitle.setText(context.getResources().getString(R.string.join));
        }
    }
}
