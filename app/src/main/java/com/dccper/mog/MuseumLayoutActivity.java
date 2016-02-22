package com.dccper.mog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MuseumLayoutActivity extends AppCompatActivity {
    private ExpandableListView exListView;
    private List<String> headerList=new ArrayList<>();
    private DBAccess dbHandler;
    private Toolbar tool;
    private HashMap<String,List<String>> child_group=new HashMap<String,List<String>>();

    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private NavSetup navSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_layout);
        headerList.add("Floor 1");
        headerList.add("Floor 2");
        headerList.add("Floor 3");
        tool=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(tool);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLay);
        listView=(ListView)findViewById(R.id.navList);
        activityTitle=getTitle().toString();
        navSetup=new NavSetup(listView,drawerLayout,activityTitle,this);
        drawerToggle=navSetup.getDrawerToggle();

        dbHandler=DBAccess.getInstance(this);
        if(!dbHandler.isOpen())
            dbHandler.open();
        new FloorListFetch().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHandler.isOpen())
            dbHandler.close();
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

    private class FloorListFetch extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(MuseumLayoutActivity.this);
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.setMessage("Loading Layout...");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MuseumLayoutActivity.this.finish();
                }
            });
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            child_group.put("Floor 1",dbHandler.getFloorInstalnList(1));
            child_group.put("Floor 2",dbHandler.getFloorInstalnList(2));
            child_group.put("Floor 3",dbHandler.getFloorInstalnList(3));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ExpandableListViewAdapter adapter=new ExpandableListViewAdapter(MuseumLayoutActivity.this,headerList,child_group);
                    exListView=(ExpandableListView)findViewById(R.id.expListView);
                    exListView.setAdapter(adapter);
                    exListView.expandGroup(0);
                    exListView.expandGroup(1);
                    exListView.expandGroup(2);
                    exListView.invalidate();
                }
            });
            dialog.dismiss();
        }
    }
}
