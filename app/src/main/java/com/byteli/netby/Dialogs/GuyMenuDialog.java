package com.byteli.netby.Dialogs;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.byteli.netby.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byte on 6/7/16.
 */
public class GuyMenuDialog {
    public GuyMenuDialog(AppCompatActivity appCompatActivity) {
        String[] labels = new String[]{"Send File", "Detail", "FileShared"};
        int[] icons = new int[]{R.mipmap.icon_game, R.mipmap.icon_guy, R.mipmap.icon_about};
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("Icon", icons[i]);
            item.put("Label", labels[i]);
            items.add(item);
        }

        ListView menu = new ListView(appCompatActivity);
        SimpleAdapter simpleAdapter = new SimpleAdapter(appCompatActivity, items, R.layout.listitem_guy_menu, new String[]{"Icon", "Label"},
                new int[]{R.id.icon, R.id.label});
        menu.setAdapter(simpleAdapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity).setTitle(R.string.app_name).setView(menu);
        builder.create().show();

    }
}
