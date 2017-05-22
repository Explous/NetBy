// INetBy.aidl
package com.byteli.netby;

import com.byteli.netby.IObject;
import com.byteli.netby.Guy;

// Declare any non-default types here with import statements

interface INetBy {
    //This aidl is for the application self
    void registerIObject(IObject iObject);
    void sendMsg(int id,String msg);
    void receiveListener(String msg);
    List<Guy> getGroup();
}
