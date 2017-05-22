package com.byteli.netby;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by byte on 1/31/16.
 */
public class NetBySDK{
    public INetBy iNetBy;
    Context context;

    ServiceConnection serviceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder){
            iNetBy = INetBy.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName){

        }
    };

    public NetBySDK(Context context){
        this.context = context;
        Intent intent = new Intent();
        intent.setAction("com.netby.service");
        intent.setPackage(context.getPackageName());
        boolean flag = context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBind(){
        context.unbindService(serviceConnection);
    }
}
