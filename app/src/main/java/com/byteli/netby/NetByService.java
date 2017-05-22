package com.byteli.netby;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.byteli.netby.Socket.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by byte on 1/31/16.
 */
public class NetByService extends Service {
    public IObject iObject;
    Client client;

    INetBy.Stub stub = new INetBy.Stub(){
        @Override
        public void registerIObject(IObject iobject){
            iObject = iobject;
        }

        @Override
        public void sendMsg(int id,String msg){
            try {
                client.sendMsg(id,msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void receiveListener(String msg){
            try {
                iObject.receiveListener(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public List<Guy> getGroup() {
            return client.group;
        }
    };

    @Override
    public IBinder onBind(Intent intent){
        System.out.println("Link to service success!");
        client = ((GlobalVariable)getApplication()).getClient();
        return stub;
    }
}
