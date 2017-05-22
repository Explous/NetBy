package com.byteli.netby;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.byteli.netby.Dialogs.ConnectDialog;
import com.byteli.netby.Dialogs.GuySettingDialog;
import com.byteli.netby.Dialogs.InfoDialog;
import com.byteli.netby.Widgets.Deploy;
import com.byteli.netby.Widgets.NetByStore;
import com.byteli.netby.Widgets.Widgets;

import static com.byteli.netby.KeyNetBy.*;

public class NetBy extends AppCompatActivity {
    Variables variables;
    Widgets widgets;
    Deploy deploy;
    NetBySDK sdk;
    NetByStore store;//SharedPrefrences Manager

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.obj.toString().charAt(0)) {
                case APP_MSG:
                    try {
                        sdk.iNetBy.receiveListener(msg.obj.toString().substring(1));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case UPDATE_GUYS:
                    System.out.println("I will update date!");
                    variables.guysAdapterd.notifyDataSetChanged();
                    break;
                case UPDATE_GUYID:
                    widgets.guyID.setText(variables.guyID);
                    break;
                case UPDATE_GUYCOUNT:
                    variables.guyCount++;
                    widgets.guyCount.setText(variables.guyCount);
                    break;
                case CLEAR_STATUSPAGER:
                    variables.statusPager.clear();
                    break;
                case APP_START:
                    if (msg.obj.toString().charAt(1) == TRUE) variables.appsPager.appManager.startAppWithPackagename(variables.client.PackageName, false);
                    else new InfoDialog(NetBy.this, getResources().getString(R.string.info_app_error));
                    break;
                default:
                    System.out.println("NetBy Error!");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_netby);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //for the android M need new permission for scanresult////////////////////////////
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M /*&& checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED*/){
            System.out.println("Request Permission Start!");
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{Manifest.permission.CAMERA}, 0x12345);
        }

        sdk = new NetBySDK(this);

        widgets = new Widgets();
        variables = new Variables(this, handler, widgets);
        store = new NetByStore(this, variables);

        deploy = new Deploy(this, this, getWindow().getDecorView(), widgets, variables);

        ((GlobalVariable)getApplication()).setClient(variables.client);

        if (store.isFirstTime()) new GuySettingDialog(this, this, variables, variables.handler);

        //Receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addDataScheme("package");
        registerReceiver(variables.appsPager.appManager.appReceiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    System.out.println(bundle.getString("result"));
                    new ConnectDialog(this, (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE), variables.wifi, variables.client, bundle.getString("result"));
                    variables.statusPager.createQRImage(bundle.getString("result"));
                    variables.statusPager.changeJoin();
                }
                break;
            case ACTION_DELETE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    System.out.println("<--------------" + bundle.toString());
                }
            default : break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        store.commit();
        sdk.unBind();
        unregisterReceiver(variables.appsPager.appManager.appReceiver);

        if(variables.server.server != null)variables.server.disabledServer();
        if(variables.client.client != null)variables.client.disabledClient();
        variables.hotspot.closeWifiAp();
    }
}
