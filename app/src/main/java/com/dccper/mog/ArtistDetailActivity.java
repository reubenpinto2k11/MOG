package com.dccper.mog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ArtistDetailActivity extends AppCompatActivity {
//    private String url="http://192.168.1.111:8081/artist_det";
    private int mArtistID;
    private ImageView artist_img;
    private TextView artist_name;
    private TextView artist_detail;
    private TextView artist_feat_art;
    private DBAccess dbHelper;
    private Toolbar tool;
    private boolean isBookmarked=false;
    private Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_detail);
        tool=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper=DBAccess.getInstance(ArtistDetailActivity.this);
        artist_img=(ImageView)findViewById(R.id.artist_det_image);
        artist_name=(TextView)findViewById(R.id.artist_det_name);
        artist_detail=(TextView)findViewById(R.id.artist_det_info);
        artist_feat_art=(TextView)findViewById(R.id.artist_detail_feat_art);
        Intent intent=getIntent();
        mArtistID=intent.getIntExtra("artist_id",0);
        if(mArtistID==0) {
            Toast.makeText(ArtistDetailActivity.this, "Invalid ID", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {
            new LoadArtist().execute(mArtistID);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHelper.isOpen())
            dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_artistdetail, menu);
        if(isBookmarked)
            menu.getItem(0).setIcon(R.drawable.book_selected);
        else
            menu.getItem(0).setIcon(R.drawable.bookmark);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.bookmark:
                if(isBookmarked) {
                    if(!dbHelper.isOpen())
                        dbHelper.open();
                    new BookmarkThings().execute("deleteBookmark");
                }
                else {
                    if(!dbHelper.isOpen())
                        dbHelper.open();
                    new BookmarkThings().execute("addBookmark");
                }
                isBookmarked=!isBookmarked;
                invalidateOptionsMenu();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return  true;
    }

    private class LoadArtist extends AsyncTask<Integer,Void,Artist>
{
    private List<String> mList=new ArrayList<>();
    private ProgressDialog progressDialog;
    String joined="";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(ArtistDetailActivity.this);
        progressDialog.setMessage("Loading Artist");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ArtistDetailActivity.this.finish();
            }
        });
        progressDialog.show();
    }

    @Override
    protected Artist doInBackground(Integer... params) {

        if(!dbHelper.isOpen())
            dbHelper.open();
        artist=dbHelper.getSingleArtist(params[0]);
        mList=dbHelper.getRelatedInst(params[0]);
        return artist;
//        mList.add(new BasicNameValuePair("artist_id", String.valueOf(mArtistID)));
//        JSONObject object=new JSONParser().makeHttpRequest(params[0],"GET",mList);
//        try {
//            object.put("details", new URLFileReader().makeHttpRequest(object.getString("text_path"), "GET"));
//        }catch (JSONException e){e.printStackTrace();}
//        return object;
    }

    @Override
    protected void onPostExecute(final Artist artist) {
        super.onPostExecute(artist);
//        final JSONObject jsonObject=o;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Picasso
//                            .with(ArtistDetailActivity.this)
//                            .load(jsonObject.getString("img_path"))
//                            .placeholder(R.drawable.stub)
//                            .into(artist_img);
//
//                    artist_name.setText(jsonObject.getString("name"));
//                    artist_feat_art.setText(jsonObject.getString("installations"));
//                    artist_detail.setText(jsonObject.getString("details"));
//                }
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
                AssetFileDescriptor descriptor;
                try {
                    InputStream imgfile=ArtistDetailActivity.this.getAssets().open(artist.getmImagePath());
//                    descriptor = ArtistDetailActivity.this.getAssets().openFd(artist.getmImagePath());
//                    Bitmap bitmap= BitmapFactory.decodeFileDescriptor(descriptor.getFileDescriptor());
                    Bitmap bitmap=BitmapFactory.decodeStream(imgfile);
                    artist_img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                artist_name.setText(artist.getmArtistName());
                for(String items:mList)
                {
                    if(joined.equals(""))
                    {
                        joined=items;
                    }
                    else
                    {
                        joined=", "+items;
                    }
                }
                artist_feat_art.setText(joined);
                if(artist.getmIsBooked()==1)
                    isBookmarked=true;
                else
                    isBookmarked=false;
                invalidateOptionsMenu();
                StringBuilder stringBuilder=new StringBuilder();
                String line;
                try {
                    Scanner sc=new Scanner(ArtistDetailActivity.this.getAssets().open(artist.getmTextPath()));
                    line=sc.nextLine();
                    while(line != null)
                    {
                        stringBuilder.append(line);
                        if(sc.hasNext())
                            line=sc.nextLine();
                        else
                            break;
                    }
                    sc.close();
                    artist_detail.setText(stringBuilder.toString());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        progressDialog.dismiss();
    }
}

    private class BookmarkThings extends AsyncTask<String,Void,Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            if(params[0].equals("addBookmark"))
            {
                dbHelper.updateArtist(artist.getnArtistID(),"bookmark");
            }
            if(params[0].equals("deleteBookmark"))
            {
                dbHelper.updateArtist(artist.getnArtistID(),"unbookmark");
            }
            return null;
        }
    }
}
