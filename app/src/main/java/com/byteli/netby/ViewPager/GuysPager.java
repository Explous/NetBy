package com.byteli.netby.ViewPager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.byteli.netby.Dialogs.GuyMenuDialog;
import com.byteli.netby.R;

/**
 * Created by byte on 2/5/16.
 */
public class GuysPager {
    AppCompatActivity appCompatActivity;
    ListView guys;

    public GuysPager(AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
    }

    public View deploy(Context context, SimpleAdapter simpleAdapter){
        View view = appCompatActivity.getLayoutInflater().inflate(R.layout.viewpager_guys, null);
        guys = (ListView)view.findViewById(R.id.guys);
        guys.setAdapter(simpleAdapter);

        guys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new GuyMenuDialog(appCompatActivity);
            }
        });

        return view;
    }

}
