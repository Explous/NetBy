package com.byteli.netby.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.byteli.netby.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byte on 5/31/16.
 */
public class AppAdapter extends SimpleAdapter {
    private LayoutInflater mInflater;
    List<Map<String, Object>> apps;
    Context context;
    int layout;

    public AppAdapter(Context context, List<Map<String, Object>> apps, int layout, String[] from, int[] to) {
        super(context, apps, layout, from, to);
        this.layout = layout;
        this.apps = apps;
        this.context = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convert, ViewGroup parent) {
        View convertView = mInflater.inflate(layout, null);
        Map<String, Object> item = (HashMap<String, Object>)apps.get(position);
        ((TextView)convertView.findViewById(R.id.label)).setText(item.get("Label").toString());
        ((ImageView)convertView.findViewById(R.id.icon)).setImageDrawable((Drawable)item.get("Icon"));
        return convertView;
    }
}
