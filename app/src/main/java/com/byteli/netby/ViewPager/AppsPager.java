package com.byteli.netby.ViewPager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.byteli.netby.AppManager;
import com.byteli.netby.Dialogs.InviteDialog;
import com.byteli.netby.R;

import java.util.List;
import java.util.Map;

import static com.byteli.netby.KeyNetBy.ACTION_DELETE;

/**
 * Created by byte on 5/24/16.
 */
public class AppsPager {
    AppCompatActivity appCompatActivity;
    public AppManager appManager;
    List<Map<String, Object>> guys;
    LinearLayout[] tabs;
    ImageView[] icons;
    GridView app;
    //boolean[] multis = new boolean[8];//for the boolean that which multi mode the app support
    boolean[] multis = new boolean[5];//for the boolean that which multi mode the app support, support 1,2,3,4 and any;
    int most = 0;//the app most player support

    int current = 0;
    public AppsPager(AppCompatActivity appCompatActivity, List<Map<String, Object>> guys) {
        this.appCompatActivity = appCompatActivity;
        this.guys = guys;
        tabs = new LinearLayout[4];
        icons = new ImageView[4];
        appManager = new AppManager(appCompatActivity);
        appManager.getPlatformApp();
    }

    public View deploy() {
        View view = appCompatActivity.getLayoutInflater().inflate(R.layout.viewpager_apps, null);
        tabs[0] = (LinearLayout)view.findViewById(R.id.app_all);
        tabs[1]= (LinearLayout)view.findViewById(R.id.app_game);
        tabs[2] = (LinearLayout)view.findViewById(R.id.app_tool);
        tabs[3] = (LinearLayout)view.findViewById(R.id.app_shop);
        icons[0] = (ImageView)view.findViewById(R.id.icon_all);
        icons[1] = (ImageView)view.findViewById(R.id.icon_game);
        icons[2] = (ImageView)view.findViewById(R.id.icon_tool);
        icons[3] = (ImageView)view.findViewById(R.id.icon_shop);
        app = (GridView)view.findViewById(R.id.app);
        app.setAdapter(appManager.simpleAdapter);
        appManager.showAllApp();

        app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ApplicationInfo appInfo = appCompatActivity.getPackageManager().getApplicationInfo(appManager.apps.get(position)
                    .get("PackageName").toString(), PackageManager.GET_META_DATA);
                        System.out.println(appInfo.metaData.getString("Multis").toString());
                        checkMultis(appInfo.metaData.getString("Multis").toString());
                        new InviteDialog(appCompatActivity, most, multis, guys, appManager.apps.get(position).get("PackageName").toString());
                    //System.out.println("<-----------------------" + appInfo.metaData.getString("NetByBe") + "  " + appManager.apps.get(position).get("PackageName"));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        app.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uri packageURI = Uri.parse("package:" + appManager.apps.get(position).get("PackageName").toString());
                Intent unistall = new Intent(Intent.ACTION_DELETE, packageURI);
                appCompatActivity.startActivityForResult(unistall, ACTION_DELETE);
                return true;
            }
        });

        tabs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != 0) {
                    restore(current);
                    current = 0;
                    icons[0].setImageResource(R.mipmap.icon_all_down);
                    appManager.showAllApp();
                }
            }
        });

        tabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != 1) {
                    restore(current);
                    current = 1;
                    icons[1].setImageResource(R.mipmap.icon_game_down);
                    appManager.showGameApp();
                }
            }
        });

        tabs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != 2) {
                    restore(current);
                    current = 2;
                    icons[2].setImageResource(R.mipmap.icon_tool_down);
                    appManager.showToolApp();
                }

            }
        });

        tabs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != 3) {
                    restore(current);
                    current = 3;
                }
                icons[3].setImageResource(R.mipmap.icon_shop);
            }
        });
        return view;
    }

    public void restore(int position){
        switch (position) {
            case 0:
                icons[0].setImageResource(R.mipmap.icon_all);
                break;
            case 1:
                icons[1].setImageResource(R.mipmap.icon_game);
                break;
            case 2:
                icons[2].setImageResource(R.mipmap.icon_tool);
                break;
            case 3:
                icons[3].setImageResource(R.mipmap.icon_shop);
                break;
            default:
                break;
        }
    }

    public void checkMultis(String multi){
        while (true) {
            switch (multi.charAt(0)) {
                case '1':
                    multis[0] = true;
                    if (most < 1) most = 1;
                    break;
                case '2':
                    multis[1] = true;
                    if (most < 2) most = 2;
                    break;
                case '3':
                    multis[2] = true;
                    if (most < 3) most = 3;
                    break;
                case '4':
                    multis[3] = true;
                    if (most < 4) most = 4;
                    break;
                case '0':
                    multis[4] = true;
                    if (most < 5) most = 8;
                    break;
                default:
                    break;
            }
            if (multi.length() != 1)multi = multi.substring(1);
            else break;
        }
    }
}
