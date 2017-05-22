package com.byteli.netby.Widgets;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.byteli.netby.Variables;

/**
 * Created by byte on 5/19/16.
 */
public class NetByStore {
    SharedPreferences store;
    SharedPreferences.Editor editor;
    Variables variables;

    public NetByStore(Context context, Variables variables) {
        this.variables = variables;
        store = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        editor = store.edit();

        variables.guyID = store.getString("GuyID", "Guy");
    }

    public boolean isFirstTime() {
        if (store.getBoolean("FirstTime", true)) {
            editor.putBoolean("FirstTime", false);
            return true;
        } else return false;
    }

    public void commit() {
        editor.putString("GuyID", variables.guyID);
        editor.commit();
    }
}
