package com.dccper.mog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by reuben.pinto2k15 on 2/8/2016.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener{
    private static MusicService mInstance=null;
    private final IBinder mIBinder=new MSBinder();
    private static MediaPlayer mediaPlayer=null;
    private int bufferPosition;
    private boolean musicThreadFinished=false;
    private boolean isForegroundRunning =false;

    Notification notification=null;
    NotificationManager manager;
    private final int NOTIFICATION_ID=1;

    @Override
    public void onCreate() {
        mInstance=this;
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                setBufferPosition(percent * getDuration() / 100);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                setMusicThreadFinished(true);
                if(isForegroundRunning) {
                    stopForeground(true);
                    isForegroundRunning=false;
                }
            }
        });
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        if(mediaPlayer != null)
            mediaPlayer.release();
        if(isForegroundRunning) {
            stopForeground(true);
            isForegroundRunning=false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    public boolean isMusicThreadFinished() {
        return musicThreadFinished;
    }

    public void setMusicThreadFinished(boolean musicThreadFinished) {
        this.musicThreadFinished = musicThreadFinished;
    }

    public void start()
    {
        mediaPlayer.start();
    }

    public void pause()
    {
        mediaPlayer.pause();
    }

    public void stop()
    {
        mediaPlayer.stop();
    }

    public void prepare() throws IOException {
        mediaPlayer.prepare();
    }

    public void reset()
    {
        mediaPlayer.reset();
    }

    public void setDataSource(FileDescriptor descriptor,long startOffset,long length) throws IOException {
        mediaPlayer.setDataSource(descriptor,startOffset,length);
    }

    public int getCurrentPosition()
    {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration()
    {
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public void seekTo(int position)
    {
        mediaPlayer.seekTo(position);
    }

    public int getBufferPosition() {
        return bufferPosition;
    }

    public void setVolume(float left_vol,float right_vol)
    {
        mediaPlayer.setVolume(left_vol,right_vol);
    }

    public void setLooping(Boolean value)
    {
        mediaPlayer.setLooping(value);
    }

    public void setBufferPosition(int bufferPosition) {
        this.bufferPosition = bufferPosition;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        start();
        if(!isForegroundRunning)
        setUpAsForeground("Audio Playing");
    }

    public static MusicService getInstance()
    {
        return mInstance;
    }

    public class MSBinder extends Binder{
        MusicService getService()
        {
            return getInstance();
        }
    }

    public void setUpAsForeground(String text)
    {
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,new Intent(getApplicationContext(),Installation.class),PendingIntent.FLAG_UPDATE_CURRENT);
        notification=new Notification();
        notification.tickerText=text;
        notification.icon=R.drawable.stub;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), text, pendingIntent);
        startForeground(NOTIFICATION_ID,notification);
        isForegroundRunning =true;
    }

}
