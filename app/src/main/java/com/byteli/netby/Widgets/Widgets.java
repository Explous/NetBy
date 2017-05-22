package com.byteli.netby.Widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byteli.netby.SlidingTabLayout;

/**
 * Created by byte on 12/2/15.
 */
public class Widgets {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    LinearLayout guySetting;//drawer LinearLayout container with icon and nickname
    public TextView guyID;//drawer textView;
    public TextView guyCount;
    public ImageView guyIcon;
    ViewPager viewPager;
    SlidingTabLayout tabLayout;
    Context context;
    AppCompatActivity appCompatActivity;

}
