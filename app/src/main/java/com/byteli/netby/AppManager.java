package com.byteli.netby;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleAdapter;

import com.byteli.netby.Adapter.AppAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byte on 5/31/16.
 */
public class AppManager {
    AppCompatActivity appCompatActivity;

    public SimpleAdapter simpleAdapter;
    public AppReceiver appReceiver = new AppReceiver();
    private int current = 0;
    public List<Map<String, Object>> apps = new ArrayList<>();
    public List<Map<String, Object>> appsAll = new ArrayList<>();
    public List<Map<String, Object>> appsGame = new ArrayList<>();
    public List<Map<String, Object>> appsTool = new ArrayList<>();

    public AppManager(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        simpleAdapter = new AppAdapter(appCompatActivity, apps, R.layout.listitem_app,
                new String[]{"icon", "label"}, new int[]{R.id.icon, R.id.label});
    }

    public void startAppWithPackagename(String packageName, Boolean isInviter) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = appCompatActivity.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)return;
        Intent resolveIntent = new Intent("netby.action.MAIN", null);
        resolveIntent.addCategory(Intent.CATEGORY_DEFAULT);
        resolveIntent.setPackage(packageInfo.packageName);

        List<ResolveInfo> resolveInfoList = appCompatActivity.getPackageManager().queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveInfo = resolveInfoList.iterator().next();
        if (resolveInfo != null) {
            ComponentName cn = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            Intent intent = new Intent();
            intent.setComponent(cn);
            intent.putExtra("isInviter", isInviter);
            intent.setAction("netby.action.MAIN");
            appCompatActivity.startActivity(intent);
        }
    }

    public void getPlatformApp() {
        Intent intent = new Intent("netby.action.MAIN");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolveInfos = appCompatActivity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        appsAll.clear();
        for (ResolveInfo resolveInfo : resolveInfos) {
            Map<String, Object> item = new HashMap<>();
            item.put("Icon", resolveInfo.loadIcon(appCompatActivity.getPackageManager()));
            item.put("Label", resolveInfo.loadLabel(appCompatActivity.getPackageManager()));
            item.put("PackageName", resolveInfo.activityInfo.packageName);
            appsAll.add(item);
        }
    }

    public void showAllApp() {
        apps.clear();
        current = 0;
        for (Map<String, Object> item : appsAll)apps.add(item);
        simpleAdapter.notifyDataSetChanged();
    }

    public void showGameApp() {
        apps.clear();
        current = 1;
        for (Map<String, Object> item : appsGame)apps.add(item);
        simpleAdapter.notifyDataSetChanged();
    }

    public void showToolApp() {
        apps.clear();
        current = 2;
        for (Map<String, Object> item : appsTool)apps.add(item);
        simpleAdapter.notifyDataSetChanged();
    }

    public final class AppReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //install app broadcast
            /*if (intent.getAction().equals("android.intent.actiom.PACKAGE_ADDED")) {

            }
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {

            }*/
            getPlatformApp();
            showAllApp();
        }
    }
}
