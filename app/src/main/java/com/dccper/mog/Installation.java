package com.dccper.mog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Installation extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private String qr="";
    private DBAccess dbHandler;
    private InstallationStruct instStruct;
    private ArrayList<Images> mList=new ArrayList<>();
    private TextView instalName;
    private TextView instalArtist;
    private TextView instalDet;
    private ViewPager pager;
    private PageIndicator indicator;
    private CustomAdapter adapter;

    private static String mSongTitle;
    private int currentPosition;
    private String currentLang = "English";
    private boolean musicThreadFinished = false;
    private boolean isBookmarked=false;
    private boolean isBound=false;
    private MusicService boundService;
    private CharSequence langs[]=new CharSequence[2];

    AssetFileDescriptor descriptor;
    Button toggle;
    Button lang;
    SeekBar seek;
    TextView curPos;
    TextView fulltime;
    TextView songTitle;

    Thread keepUp= new Thread(new Runnable() {
        @Override
        public void run() {
            currentPosition=0;
            if(isBound) {
                while (!boundService.isMusicThreadFinished()) {
                    try {
                        Thread.sleep(100);
//                        currentPosition=mMediaPlayer.getCurrentPosition();
                        if (isBound)
                            currentPosition = boundService.getCurrentPosition();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int total;
                    if (isBound)
                        total = boundService.getDuration();
                    else
                        total = 0;
                    final String totalTime = getAsTime(total);
                    final String curTime = getAsTime(currentPosition);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seek.setMax(total);
                            seek.setProgress(currentPosition);
                            if (isBound)
                                seek.setSecondaryProgress(boundService.getBufferPosition());
                            if (isBound)
                                if (boundService.isPlaying()) {
                                    toggle.setBackgroundResource(R.drawable.ic_pause);
                                } else {
                                    toggle.setBackgroundResource(R.drawable.ic_play);
                                }
                            curPos.setText(curTime);
                            fulltime.setText(totalTime);
                        }
                    });
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);
        Toolbar tool=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        instalName=(TextView)findViewById(R.id.instal_title);
        instalArtist=(TextView)findViewById(R.id.artist_name1);
        instalDet=(TextView)findViewById(R.id.instal_det);
        pager=(ViewPager)findViewById(R.id.viewpager_inst);
        indicator=(CirclePageIndicator)findViewById(R.id.indicator_inst);
        Intent intent=getIntent();
        qr=intent.getStringExtra("qr_tag");
        dbHandler=DBAccess.getInstance(this);
        if(!dbHandler.isOpen())
            dbHandler.open();
        new BackFetchTask().execute(qr);
        toggle=(Button)findViewById(R.id.play_pause);
        songTitle=(TextView)findViewById(R.id.song_title);
        seek=(SeekBar)findViewById(R.id.seek);
        seek.setOnSeekBarChangeListener(this);
        lang=(Button)findViewById(R.id.lang);
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMediaPlayer.pause();
                boundService.pause();
                setLangs(currentLang);
                AlertDialog.Builder builder=new AlertDialog.Builder(Installation.this);
                builder.setTitle("Change Audio Language to :");
                builder.setItems(langs,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            if(langs[which].equals("English"))
                            {
                                descriptor=getAssets().openFd(instStruct.getEng_aud().getmAud_path());
                                currentLang="English";
                                mSongTitle=instStruct.getEng_aud().getmAud_name();
                            }
                            if(langs[which].equals("Hindi")) {
                                descriptor = getAssets().openFd(instStruct.getHin_aud().getmAud_path());
                                currentLang="Hindi";
                                mSongTitle=instStruct.getHin_aud().getmAud_name();
                            }
                            if(langs[which].equals("Marathi")) {
                                descriptor = getAssets().openFd(instStruct.getMar_aud().getmAud_path());
                                currentLang="Marathi";
                                mSongTitle=instStruct.getMar_aud().getmAud_name();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        boundService.stop();
                        boundService.reset();
                        try {
                            boundService.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
                            boundService.prepare();
                            songTitle.setText(mSongTitle);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
            }
        });
        curPos=(TextView)findViewById(R.id.currpos);
        fulltime=(TextView)findViewById(R.id.dur);

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boundService.isPlaying()) {
                    boundService.pause();
                    toggle.setBackgroundResource(R.drawable.ic_play);
                }
                else {
                    boundService.start();
                    toggle.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!isBound)
            doBindService();

        try {
            descriptor=getAssets().openFd("audio/crowd-cheering.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dbHandler.isOpen())
            dbHandler.close();
        if(isBound) {
            doUnbindService();
            isBound=false;
        }
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(Installation.this);
        builder.setTitle("Warning!");
        builder.setMessage("Press Proceed to stop the audio and go back\nPress Cancel to stay on the same page");
        builder.setPositiveButton("Proceed",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isBound) {
                    doUnbindService();
                    isBound=false;
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_installation, menu);
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
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.bookmark:
                if(isBookmarked) {
                    if(!dbHandler.isOpen())
                        dbHandler.open();
                    new BookmarkThings().execute("deleteBookmark");
                }
                else {
                    if(!dbHandler.isOpen())
                        dbHandler.open();
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

    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundService=(MusicService.getInstance());
            isBound=true;
            try {
                if(!boundService.isPlaying())
                    boundService.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(),descriptor.getLength());
                descriptor.close();
                if(!boundService.isPlaying())
                    boundService.prepare();
                songTitle.setText(mSongTitle);
            } catch (IOException e) {
                e.printStackTrace();
            }
            boundService.setVolume(1f,1f);
            boundService.setLooping(false);
            if(!keepUp.isAlive())
                keepUp.start();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundService=null;
        }
    };

    void doBindService()
    {
        bindService(new Intent(Installation.this,MusicService.class),connection,BIND_AUTO_CREATE);

    }

    void doUnbindService()
    {
        if(isBound)
            unbindService(connection);
        stopService(new Intent(Installation.this,MusicService.class));
        isBound=false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser)
            boundService.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private String getAsTime(int time){
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes((long) time),
                TimeUnit.MILLISECONDS.toSeconds((long) time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) time)));
    }

    private void setLangs(String current)
    {
        if(current.equals("English"))
        {
            langs[0]="Hindi";langs[1]="Marathi";
        }
        if(current.equals(("Hindi")))
        {
            langs[0]="English";langs[1]="Marathi";
        }
        if (current.equals("Marathi"))
        {
            langs[0]="English";langs[1]="Hindi";
        }
    }

    private class BackFetchTask extends AsyncTask<String,Void,Void>
    {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(Installation.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Installation.this.finish();
                }
            });
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            instStruct=dbHandler.getInstallation(params[0]);
            instStruct.setEng_aud(dbHandler.resetAudioDat(instStruct.getEng_aud()));
            instStruct.setHin_aud(dbHandler.resetAudioDat(instStruct.getHin_aud()));
            instStruct.setMar_aud(dbHandler.resetAudioDat(instStruct.getMar_aud()));
            mList=dbHandler.getInstImages(instStruct.get_id());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    instalName.setText(instStruct.getInst_name());
                    instalArtist.setText("by "+instStruct.getArtst_name());
                    adapter=new CustomAdapter(Installation.this,null,mList);
                    pager.setAdapter(adapter);
                    indicator.setViewPager(pager);
                    if(instStruct.getmIsBooked()==1)
                        isBookmarked=true;
                    else
                        isBookmarked=false;
                    invalidateOptionsMenu();
                    StringBuilder stringBuilder=new StringBuilder();
                    String line;
                    try {
                        Scanner sc=new Scanner(Installation.this.getAssets().open(instStruct.getText_path()));
                        line=sc.nextLine();
                        while(line != null)
                        {
                            stringBuilder.append(line);
//                            line=sc.nextLine();
                            if(sc.hasNext())
                                line=sc.nextLine();
                            else
                                break;
                        }
                        sc.close();
                        instalDet.setText(stringBuilder.toString());
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    try {
                        descriptor=getAssets().openFd(instStruct.getEng_aud().getmAud_path());
                        mSongTitle=instStruct.getEng_aud().getmAud_name();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(!isBound)
                        doBindService();
                }
            });
            dialog.dismiss();
        }
    }

    private class BookmarkThings extends AsyncTask<String,Void,Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            if(params[0].equals("addBookmark"))
            {
                dbHandler.updateInstallation(instStruct.get_id(),"bookmark");
            }
            if(params[0].equals("deleteBookmark"))
            {
                dbHandler.updateInstallation(instStruct.get_id(),"unbookmark");
            }
            return null;
        }
    }
}
