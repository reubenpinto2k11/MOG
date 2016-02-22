package com.dccper.mog;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;

/**
 * Created by reuben.pinto2k15 on 1/28/2016.
 */
public class NavSetup {
    private ListView listView;
    private LinkedList<NavObjects> list=new LinkedList<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListAdapter adapter;
    private String activityTitle;
    private Activity activity;
    private Intent navIntent;


    public NavSetup(ListView listView, DrawerLayout drawerLayout, String activityTitle, Activity activity) {
        this.listView = listView;
        this.drawerLayout = drawerLayout;
        this.activityTitle = activityTitle;
        this.activity = activity;
        populate_draw();
        addDrawerItems();
        setupDrawer();
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    private void populate_draw()
    {
        list.add(new NavObjects(R.drawable.splash_draw,null));
        list.add(new NavObjects(R.drawable.nav_home,"Homepage"));
        list.add(new NavObjects(R.drawable.nav_map,"Museum Layout"));
        list.add(new NavObjects(R.drawable.nav_user,"About the Artists"));
        list.add(new NavObjects(R.drawable.nav_calender,"Upcoming Events"));
        list.add(new NavObjects(R.drawable.nav_book,"Bookmarks"));
        list.add(new NavObjects(R.drawable.nav_info,"Contact Us"));
    }

    private void addDrawerItems()
    {

        adapter=new ListAdapter(activity,list);
        listView.setAdapter(adapter);

        //write onclicklistener to send intents to other pages here
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 1://homepage
                        if(activityTitle.equals("Homepage"))
                        {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        }
                        else
                        {
                           // drawerLayout.closeDrawer(Gravity.LEFT);
//                            navIntent=new Intent(activity,HomePage.class);
//                            activity.startActivity(navIntent);
                            activity.finish();
                            break;
                        }
                    case 2://museum layout
                        if(activityTitle.equals("Museum Layout"))
                        {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        }
                        else
                        {//change the destination class when created
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            navIntent=new Intent(activity,MuseumLayoutActivity.class);
                            activity.startActivity(navIntent);
                            if(!activityTitle.equals("Homepage"))
                               activity.finish();
                            break;
                        }
                    case 3://About the artists
                        if(activityTitle.equals("About the Artists"))
                        {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        }
                        else
                        {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            navIntent=new Intent(activity,ArtistList.class);
                            activity.startActivity(navIntent);
                            if(!activityTitle.equals("Homepage"))
                                activity.finish();
                            break;
                        }
                    case 4://Upcoming Events
//                        if(activityTitle.equals("Upcoming Events"))
//                        {
//                            drawerLayout.closeDrawer(Gravity.LEFT);
//                            break;
//                        }
//                        else
//                        {//change destination class when created
//                          drawerLayout.closeDrawer(Gravity.LEFT);
//                            navIntent=new Intent(activity,Installation.class);
//                            activity.startActivity(navIntent);
//                            if(!activityTitle.equals("Homepage"))
//                                activity.finish();
//                            break;
//                        }
                        break;

                    case 5://Bookmarks
                        if(activityTitle.equals("Bookmarks"))
                        {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        }
                        else
                        {//change destination class when created
//                            navIntent=new Intent(activity,ArtistList.class);
//                            activity.startActivity(navIntent);
//                            if(!activityTitle.equals("Homepage"))
//                                activity.finish();
                            break;
                        }

                    case 6://Contact Us
                        if(activityTitle.equals("Contact Us"))
                        {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        }
                        else
                        {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                            navIntent=new Intent(activity,ContactUs.class);
                            activity.startActivity(navIntent);
                            if(!activityTitle.equals("Homepage"))
                                activity.finish();
                            break;
                        }

                    default:
                        break;
                }
            }
        });
    }

    private void setupDrawer()
    {
        drawerToggle=new ActionBarDrawerToggle(activity,drawerLayout,R.string.open_drawer,R.string.close_drawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                activity.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.invalidateOptionsMenu();

            }


        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }
}
