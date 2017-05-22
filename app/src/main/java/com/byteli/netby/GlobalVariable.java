package com.byteli.netby;

import android.app.Application;

import com.byteli.netby.Socket.Client;

/**
 * Created by byte on 2/1/16.
 */
public class GlobalVariable extends Application {
    Client client;
    public void setClient(Client client){
        this.client = client;
    }
    public Client getClient(){
        return client;
    }
}
