package com.byteli.netby.Socket;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.RemoteException;

import com.byteli.netby.GlobalVariable;
import com.byteli.netby.Guy;
import com.byteli.netby.NetBySDK;
import com.byteli.netby.NetWork.Hotspot;
import com.byteli.netby.NetWork.Wifi;
import com.byteli.netby.Variables;
import com.byteli.netby.Widgets.Widgets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.byteli.netby.KeyNetBy.*;

/**
 * Created by byte on 1/31/16.
 */
public class Client {

    public Socket client = null;
    private NetBySDK sdk;
    Hotspot hotspot;
    Wifi wifi;
    public DataOutputStream outputStream = null;
    DataInputStream inputStream = null;
    Handler handler;
    Context context;

    public boolean isHost = false;
    public boolean enabled = false;
    int tryConnect = 0;
    public int myID = 0;

    public List<Guy> group = new ArrayList<>();
    public String PackageName = null;

    Widgets widgets;
    Variables variables;

    public Client(Context context, Hotspot hotspot, Wifi wifi, Handler handler, Widgets widgets, Variables variables){
        this.context = context;
        this.hotspot = hotspot;
        this.wifi = wifi;
        this.handler = handler;
        this.widgets = widgets;
        this.variables = variables;

    }

    public boolean enabledClient(String Ip) {
        try {
            client = new Socket(Ip, 8086);
            enabled = true;
            outputStream = new DataOutputStream(client.getOutputStream());
            inputStream = new DataInputStream(client.getInputStream());
            outputStream.writeUTF(variables.guyID);
            myID = Integer.parseInt(inputStream.readUTF());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String msg;
                    try {
                        while (true) {
                            msg = inputStream.readUTF();
                            System.out.println("ClientMsg = " + msg);
                            switch (msg.charAt(0)) {
                                case HEAD_GUY:
                                    switch (msg.charAt(1)) {
                                        case GUY_NEW:
                                            Map<String, Object> temp = new HashMap<>();
                                            temp.put("ID", Integer.parseInt(msg.substring(2,4)));
                                            temp.put("Label", msg.substring(4));
                                            variables.guys.add(temp);
                                            System.out.println("I have recv a new guy!");
                                            handler.sendMessage(handler.obtainMessage(1, UPDATE_GUYS));
                                            break;
                                        case GUY_DELETE:
                                            deleteGuy(Integer.parseInt(msg.substring(2)));
                                            break;
                                    }
                                    break;
                                case HEAD_APP:
                                    switch (msg.charAt(1)) {
                                        case APP_MSG:
                                            handler.sendMessage(handler.obtainMessage(1, "" + APP_MSG + msg.substring(msg.lastIndexOf(MARK) + 1)));
                                            break;
                                        case APP_INVITE:
                                            try {
                                                //check wether install the app which be invited to play
                                                context.getPackageManager().getPackageInfo(msg.substring(2, msg.indexOf(MARK)), PackageManager.GET_ACTIVITIES);
                                                outputStream.writeUTF("" + HEAD_APP + APP_CHECK + TRUE);

                                                group.clear();
                                                PackageName = msg.substring(2, msg.indexOf(MARK));
                                                String beinvited = msg.substring(msg.indexOf(MARK) + 1);
                                                int num = beinvited.length() / 2;
                                                for (int temp = 0; temp < num; temp ++) {
                                                    int idTemp = Integer.parseInt(beinvited.substring(2 * temp, (2 * temp) + 2));
                                                    if (myID == temp) continue;
                                                    for (Map<String, Object> map : variables.guys)
                                                        if ((int)map.get("ID") == idTemp)
                                                            group.add(new Guy(idTemp, map.get("Label").toString()));
                                                }
                                            } catch (PackageManager.NameNotFoundException e) {
                                                outputStream.writeUTF("" + HEAD_APP + APP_CHECK + FALSE);
                                                e.printStackTrace();
                                            }
                                            break;
                                        case APP_START:
                                            if (msg.charAt(2) == TRUE)
                                                handler.sendMessage(handler.obtainMessage(1, "" + APP_START + TRUE));
                                            else handler.sendMessage(handler.obtainMessage(1, "" + APP_START + FALSE));
                                            break;
                                        default:
                                            break;
                                    }
                                    break;
                                default:
                                    handler.sendMessage(handler.obtainMessage(1, msg));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Server connection down!");
                        disabledClient();
                    }
                }
            }).start();
        } catch (IOException e) {
            System.out.println("Client Fuck!");
            tryConnect++;
            if (tryConnect != 3) enabledClient(Ip);
            else return false;
        }
        return true;
    }

    public void disabledClient(){
        handler.sendMessage(handler.obtainMessage(1, CLEAR_STATUSPAGER));
        variables.guys.clear();
        handler.sendMessage(handler.obtainMessage(1, UPDATE_GUYS));
        if(client != null) try {
            if(inputStream != null)inputStream.close();
            if(outputStream != null)outputStream.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void deleteGuy(int id){
        int num=0;
        for (Map<String, Object> tempMap : variables.guys)
            if ((int) tempMap.get("ID") == id)break;
            else num++;
        variables.guys.remove(num);
        handler.sendMessage(handler.obtainMessage(1, UPDATE_GUYS));
    }

    public void sendMsg(int id,String msg) throws IOException {
        outputStream.writeUTF("" + HEAD_APP + APP_MSG + id + msg);
    }
}
