package com.dccper.mog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by reuben.pinto2k15 on 1/27/2016.
 */
public class ContactUs extends AppCompatActivity
{
    Toolbar tool;

    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private NavSetup navSetup;
    private TextView phone;
    private TextView fb;
    private TextView email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        tool=(Toolbar)findViewById(R.id.toolbar);

        phone=(TextView)findViewById(R.id.museum_ph_no);
        fb=(TextView)findViewById(R.id.museum_fb);
        email=(TextView)findViewById(R.id.museum_email_addr);

        setSupportActionBar(tool);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLay);
        listView=(ListView)findViewById(R.id.navList);
        activityTitle=getTitle().toString();
        navSetup=new NavSetup(listView,drawerLayout,activityTitle,this);
        drawerToggle=navSetup.getDrawerToggle();

        Linkify.addLinks(phone,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(fb,Linkify.WEB_URLS);
        Linkify.addLinks(email,Linkify.EMAIL_ADDRESSES);
    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT))
        {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}
