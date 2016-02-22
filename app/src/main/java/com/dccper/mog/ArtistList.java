package com.dccper.mog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ArtistList extends AppCompatActivity {
    private RecyclerView rv;
    ViewAdapter adapter;
    JSONParser parser=new JSONParser();
    DBAccess dbHandler;
    private List<Artist> mList=new ArrayList<>();
    private Toolbar tool;
//    private String url="http://192.168.1.111:8081/artist_list";

    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private NavSetup navSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclable_layout);
        tool=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLay);
        listView=(ListView)findViewById(R.id.navList);
        activityTitle=getTitle().toString();
        navSetup=new NavSetup(listView,drawerLayout,activityTitle,this);
        drawerToggle=navSetup.getDrawerToggle();

        rv=(RecyclerView)findViewById(R.id.recview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(ArtistList.this);
        rv.setLayoutManager(layoutManager);
        adapter = new ViewAdapter(mList, ArtistList.this);
        rv.setAdapter(adapter);
        rv.setVisibility(View.VISIBLE);
        new ArtistAsyncHandler().execute();
        //populate();
//        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
//        if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
//            new ArtistAsyncHandler().execute(url);
//        }
//        else
//        {
//            Toast.makeText(getBaseContext(), "NO NETWORK PRESENT", Toast.LENGTH_SHORT).show();
//        }
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

    private class ArtistAsyncHandler extends AsyncTask<String,Void,List<Artist>>
   {
       ProgressDialog progressDialog;
//       InputStream inputStream;
//       String result="";

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           progressDialog=new ProgressDialog(ArtistList.this);
           progressDialog.setIndeterminate(false);
           progressDialog.setCancelable(true);
           progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
               @Override
               public void onCancel(DialogInterface dialog) {
                   finish();
               }
           });
           progressDialog.setMessage("Fetching Artists...");
           progressDialog.show();
       }

       @Override
       protected List<Artist> doInBackground(String... params) {
           List<Artist> artList=new ArrayList<>();
           dbHandler=DBAccess.getInstance(ArtistList.this);
           if(!dbHandler.isOpen())
               dbHandler.open();
           artList=dbHandler.getArtists();
           return artList;
       }

       @Override
       protected void onPostExecute(final List<Artist> artists) {
           super.onPostExecute(artists);
//           if(progressDialog.isShowing()) {
//               progressDialog.dismiss();
//               adapter = new ViewAdapter(mList, ArtistList.this);
//               rv.setAdapter(adapter);
//           }
//           else
//           {
//               adapter = new ViewAdapter(mList, ArtistList.this);
//               rv.setAdapter(adapter);
//           }
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   rv=(RecyclerView)findViewById(R.id.recview);
                   LinearLayoutManager layoutManager=new LinearLayoutManager(ArtistList.this);
                   rv.setLayoutManager(layoutManager);
//                   adapter = new ViewAdapter(mList, ArtistList.this);
//                   rv.setAdapter(adapter);
//                   rv.setVisibility(View.VISIBLE);
                   adapter = new ViewAdapter(artists,ArtistList.this);
                   rv.setAdapter(adapter);
                   rv.invalidate();
               }
           });
           progressDialog.dismiss();
       }
   }
}
