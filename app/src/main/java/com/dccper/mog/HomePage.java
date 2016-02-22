package com.dccper.mog;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class HomePage extends AppCompatActivity {

    //reqd for main content
    private ArrayList<Integer> mlist= new ArrayList<>();
    private PageIndicator indicator;
    private ViewPager pager;
    private Toolbar tool;
    private Button scan_btn;


    //reqd for navdrawer
    private ListView listView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private NavSetup navSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //set toolbar & toolbar actions
        tool=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //navigation drawer setup
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLay);
        listView=(ListView)findViewById(R.id.navList);
        activityTitle=getTitle().toString();
        navSetup=new NavSetup(listView,drawerLayout,activityTitle,this);
        drawerToggle=navSetup.getDrawerToggle();

        //Homepage main content setup
        pager=(ViewPager)findViewById(R.id.viewpager);
        indicator=(CirclePageIndicator)findViewById(R.id.indicator);
        populate_pager();
        CustomAdapter adapter=new CustomAdapter(HomePage.this,mlist,null);
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        //set button to scan
        scan_btn=(Button)findViewById(R.id.btn_scan);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator=new IntentIntegrator(HomePage.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCaptureActivity(AnyOrientationCapture.class);
                integrator.setPrompt("Scan a QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
                Toast.makeText(HomePage.this, "Scan Failed", Toast.LENGTH_SHORT).show();
            else {
                if (Pattern.matches("[Mm][Oo][Gg].*", result.getContents()))
                {
                    Intent intent=new Intent(HomePage.this,Installation.class);
                    intent.putExtra("qr_tag",result.getContents());
                    startActivity(intent);
                }
                else
                    Toast.makeText(HomePage.this, "Scanned : " + result.getContents()+" : Invalid Tag", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void populate_pager()
    {
        mlist.add(R.drawable.sample1);
        mlist.add(R.drawable.sample2);
        mlist.add(R.drawable.sample3);
        mlist.add(R.drawable.sample4);
        mlist.add(R.drawable.sample5);
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
