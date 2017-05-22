package com.byteli.netby;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.SimpleAdapter;

import com.byteli.netby.NetWork.Hotspot;
import com.byteli.netby.NetWork.Wifi;
import com.byteli.netby.Socket.Client;
import com.byteli.netby.Socket.Server;
import com.byteli.netby.ViewPager.AppsPager;
import com.byteli.netby.ViewPager.StatusPager;
import com.byteli.netby.Widgets.Widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by byte on 12/2/15.
 */
public class Variables {
    public Wifi wifi;
    public Hotspot hotspot;
    public Server server;
    public Client client;
    public StatusPager statusPager;
    public AppsPager appsPager;
    public boolean host;

    public SimpleAdapter guysAdapterd;
    public List<Map<String, Object>> guys = new ArrayList<>();
    public List<View> viewPagers = new ArrayList<>();
    public Handler handler;//the handler in the main thread

    public String guyID = null;//the id of this guy
    public int guyCount = 0;//how many guys connect together

    Variables(Context context, Handler handler, Widgets widgets){
        this.handler=handler;

        wifi = new Wifi((android.net.wifi.WifiManager)context.getSystemService(Context.WIFI_SERVICE));

        hotspot = new Hotspot((android.net.wifi.WifiManager)context.getSystemService(Context.WIFI_SERVICE));
        server = new Server(wifi, context);
        client = new Client(context, hotspot, wifi, handler, widgets, this);

        guysAdapterd = new SimpleAdapter(context, guys, R.layout.listitem_guy, new String[]{"Label"}, new int[]{R.id.title});
    }
}
