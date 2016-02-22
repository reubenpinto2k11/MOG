package com.dccper.mog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread sleepTimer=new Thread(){
          public void run()
          {
              try{
                  sleep(3500);
              }
              catch (InterruptedException e)
              {
                  e.printStackTrace();
              }
              finally {
                  SharedPreferences preferences=getApplicationContext().getSharedPreferences("MOGPref", Context.MODE_PRIVATE);
                  //change default to false
                  if(preferences.getBoolean("userinfo_executed",true))
                  {
                      Intent intent=new Intent(Splash.this,HomePage.class);
                      startActivity(intent);
                  }
                  else
                  {
                      Intent intent = new Intent(Splash.this, UserInfo.class);
                      startActivity(intent);
                  }
              }
          }
        };
        sleepTimer.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
