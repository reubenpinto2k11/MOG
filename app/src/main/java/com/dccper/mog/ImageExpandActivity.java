package com.dccper.mog;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class ImageExpandActivity extends AppCompatActivity {
    ZoomCustomAdapter adapter;
    List<Images> mList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_expand);
        ExtendedViewPager pager=(ExtendedViewPager)findViewById(R.id.img_zoom_pager);
        Intent intent=getIntent();
        mList=intent.getParcelableArrayListExtra("img_array");
        adapter=new ZoomCustomAdapter(this,mList);
        pager.setAdapter(adapter);
        pager.setCurrentItem(intent.getIntExtra("curr_item",0));
    }

}
