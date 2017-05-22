package com.byteli.netby.Socket;

import android.content.Context;

import com.byteli.netby.NetWork.Wifi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.byteli.netby.KeyNetBy.*;

/**
 * Created by byte on 1/31/16.
 */
public class Server {
    Wifi wifi;
    Context context;
    public ServerSocket server = null;
    public List<Map<String, Object>> guys = new ArrayList<>();//for list of guys
    private int id = 9;

    public boolean enabled = false;

    List<Map<String, Object>> guysSwitch = new ArrayList<>();//only used to quick pick the item by id index,do not remove any item.the index is the guyID - 10;
    //List<Map<String, Object>> group = new ArrayList<>();//store the guys playing a app
    List<Integer> group = new ArrayList<>();
    List<Integer> appCheck = new ArrayList<>();//for check wether the guys be invited have install the app
    String PackageName = null;

    public Server(Wifi wifi, Context context){
        this.wifi = wifi;
        this.context = context;
    }

    public void enabledServer(){
        try {
            //check the wifiap whether is be opened already
            wifi.checkWifiApState();

            System.out.println("Enabled Server Start!");
            server = new ServerSocket(8086);
            System.out.println("Enabled Server Successs!");
            enabled = true;

            while (true) {
                final Socket accept = server.accept();
                final DataOutputStream outputStream = new DataOutputStream(accept.getOutputStream());
                final DataInputStream inputStream = new DataInputStream(accept.getInputStream());
                //get the guys name
                String name = inputStream.readUTF();
                id++;
                Map<String, Object> item = new HashMap<>();
                item.put("ID", id);
                item.put("Name", name);
                item.put("Socket", accept);
                item.put("OutputStream", outputStream);
                item.put("InputStream", inputStream);

                outputStream.writeUTF("" + id);

                for (Map<String, Object> temp : guys) {//Bradcast the new guy connect and send the guys connected to the new guy
                    ((DataOutputStream) temp.get("OutputStream")).writeUTF("" + HEAD_GUY + GUY_NEW + id + name);
                    System.out.println("" + HEAD_GUY + GUY_NEW + id + name);
                    outputStream.writeUTF("" + HEAD_GUY + GUY_NEW + temp.get("ID") + temp.get("Name"));
                }

                guys.add(item);
                guysSwitch.add(item);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msg;
                        int ID = id;
                        try {
                            while (true) {
                                msg = inputStream.readUTF();
                                System.out.println("Msg:" + msg);
                                switch (msg.charAt(0)) {
                                    case HEAD_APP:
                                        switch (msg.charAt(1)) {
                                            case APP_MSG:
                                                int target = Integer.parseInt(msg.substring(2, 4));
                                                ((DataOutputStream)guysSwitch.get(target - 10).get("OutputStream"))
                                                        .writeUTF("" + HEAD_APP + APP_MSG + guysSwitch.get(target - 10).get("Name") + MARK + msg.substring(4));
                                                break;
                                            case APP_INVITE:
                                                group.clear();
                                                appCheck.clear();
                                                String beinvited = msg.substring(msg.lastIndexOf(MARK) + 1);
                                                int num = beinvited.length() / 2;
                                                for (int temp = 0; temp < num; temp ++)
                                                    group.add(Integer.parseInt(beinvited.substring(2 * temp, (2 * temp) + 2)));
                                                PackageName = msg.substring(2, msg.lastIndexOf(MARK));
                                                for (int temp : group)
                                                    ((DataOutputStream)guysSwitch.get(temp - 10).get("OutputStream")).writeUTF(msg);
                                                break;
                                            case APP_CHECK:
                                                appCheck.add(Integer.parseInt(msg.substring(2)));
                                                if (appCheck.size() == group.size()) {
                                                    boolean flag = true;
                                                    for (int temp : appCheck) if (temp == 1) {
                                                        flag = false;
                                                        break;
                                                    }
                                                    if (flag) for (int temp : group)
                                                        ((DataOutputStream)guysSwitch.get(temp - 10).get("OutputStream")).writeUTF("" + HEAD_APP + APP_START + TRUE);
                                                    else for (int temp : group)
                                                        ((DataOutputStream)guysSwitch.get(temp - 10).get("OutputStream")).writeUTF("" + HEAD_APP + APP_START + FALSE);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    case HEAD_GUY:
                                        switch (msg.charAt(1)) {
                                            case '0':
                                        }
                                        break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Guy disconnect!");
                            if (enabled) {
                                deleteGuy(ID);
                                for (Map<String, Object> temp : guys)
                                    try {
                                        ((DataOutputStream) temp.get("OutputStream")).writeUTF("" + HEAD_GUY + GUY_DELETE + id);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disabledServer() {
        try {
            enabled = false;
            server.close();
            clearGuys();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearGuys() throws IOException {
        for (Map<String, Object> temp : guys) {
            ((DataOutputStream) temp.get("OutputStream")).close();
            ((DataInputStream) temp.get("InputStream")).close();
            ((Socket) temp.get("Socket")).close();
        }
        guys.clear();
        guysSwitch.clear();
        id = 9;
        enabled = false;
    }

    private void deleteGuy(int id){
        int num=0;
        System.out.println("<----------" + id);
        for (Map<String, Object> tempMap : guys)
            if ((int) tempMap.get("ID") == id)
                try {
                    ((DataOutputStream) tempMap.get("OutputStream")).close();
                    ((DataInputStream) tempMap.get("InputStream")).close();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else {
                System.out.println((int)tempMap.get("ID") + "  " + id);
                num++;
            }
        guys.remove(num);
    }
}
