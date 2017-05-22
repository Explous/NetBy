package com.byteli.netby.Widgets;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byteli.netby.Dialogs.GuySettingDialog;
import com.byteli.netby.R;
import com.byteli.netby.SlidingTabLayout;
import com.byteli.netby.Variables;
import com.byteli.netby.ViewPager.AppsPager;
import com.byteli.netby.ViewPager.GuysPager;
import com.byteli.netby.ViewPager.StatusPager;

/**
 * Created by byte on 1/1/16.
 */
public class Deploy {
    Widgets widgets;
    Variables variables;

    public Deploy(final Context context, final AppCompatActivity appCompatActivity, View decorView, final Widgets widgets, final Variables variables){
        this.widgets = widgets;
        this.variables = variables;

        //toolbar///////////////////////////////////////////////////////////////////////////////////
        widgets.guyCount = (TextView)decorView.findViewById(R.id.guy_count);
        widgets.context = context;
        widgets.appCompatActivity = appCompatActivity;
        widgets.toolbar = (Toolbar) decorView.findViewById(R.id.toolbar);
        widgets.toolbar.setTitle("");
        View view = appCompatActivity.getLayoutInflater().inflate(R.layout.custom_actionbar,null);
        widgets.toolbar.addView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        appCompatActivity.setSupportActionBar(widgets.toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        widgets.drawerLayout = (DrawerLayout) decorView.findViewById(R.id.drawer);
        widgets.drawerToggle = new ActionBarDrawerToggle(appCompatActivity, widgets.drawerLayout, widgets.toolbar, R.string.drawer_open,
                R.string.drawer_close);
        widgets.drawerToggle.syncState();
        widgets.drawerLayout.setDrawerListener(widgets.drawerToggle);

        //SlidingTab////////////////////////////////////////////////////////////////////////////////
        deployViews(context);
        widgets.tabLayout = (SlidingTabLayout)decorView.findViewById(R.id.sliding_tabs);
        widgets.viewPager = (ViewPager)decorView.findViewById(R.id.view_pager);
        widgets.viewPager.setOffscreenPageLimit(3);
        widgets.tabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        widgets.tabLayout.setSelectedIndicatorColors(context.getResources().getColor(R.color.android));
        widgets.tabLayout.setDistributeEvenly(true);
        widgets.viewPager.setAdapter(new MainTabs());
        widgets.tabLayout.setViewPager(widgets.viewPager);
        if (widgets.tabLayout != null) {
            widgets.tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
                    //AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.7f);
                    //alphaAnimation.setDuration(500);
                    //alphaAnimation.setFillAfter(true);
                    //widgets.fabMenu.startAnimation(alphaAnimation);
        //Drawer////////////////////////////////////////////////////////////////////////////////////
        new Drawer(context, decorView);
        widgets.guyID = (TextView) decorView.findViewById(R.id.guy_id);
        widgets.guyID.setText(variables.guyID);
        widgets.guySetting = (LinearLayout) decorView.findViewById(R.id.guy_setting);
        widgets.guyIcon =(ImageView) decorView.findViewById(R.id.guy_icon);
        widgets.guySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GuySettingDialog(context, appCompatActivity, variables, variables.handler);
            }
        });

    }

    void deployViews(Context context){
        // Inflate a new layout from our resources
        variables.statusPager = new StatusPager(widgets.appCompatActivity);
        variables.appsPager = new AppsPager(widgets.appCompatActivity, variables.guys);
        variables.viewPagers.add(variables.statusPager.deploy(context, widgets, variables.hotspot, variables.server, variables.client));

        variables.viewPagers.add(variables.appsPager.deploy());

        variables.viewPagers.add(new GuysPager(widgets.appCompatActivity).deploy(context, variables.guysAdapterd));
    }

    class MainTabs extends PagerAdapter {

        SparseArray<View> views = new SparseArray<View>();

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + (position + 1);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            // Add the newly created View to the ViewPager
            container.addView(variables.viewPagers.get(position));

            views.put(position, variables.viewPagers.get(position));

            // Return the View
            return variables.viewPagers.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            views.remove(position);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

    }
}
