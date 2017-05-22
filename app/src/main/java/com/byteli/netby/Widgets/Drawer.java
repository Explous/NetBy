package com.byteli.netby.Widgets;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.byteli.netby.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byte on 5/18/16.
 */
public class Drawer {
    public Drawer(Context context, View decorView) {
        List<Map<String, Object>> menu = new ArrayList<>();
        String[] titles = new String[]{context.getResources().getString(R.string.setting), context.getResources().getString(R.string.theme), context.getResources().getString(R.string.about)};
        int[] icons = new int[]{R.mipmap.icon_setting, R.mipmap.icon_theme, R.mipmap.icon_about};
        for (int i = 0; i < 3; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("icon", icons[i]);
            item.put("title",titles[i]);
            menu.add(item);
        }

        ListView listDrawer = (ListView)decorView.findViewById(R.id.list_drawer);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, menu, R.layout.listitem_drawer, new String[]{"icon", "title"}, new int[]{R.id.icon, R.id.title});
        listDrawer.setAdapter(simpleAdapter);
    }
}
