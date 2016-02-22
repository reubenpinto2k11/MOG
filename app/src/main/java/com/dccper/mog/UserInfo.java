package com.dccper.mog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserInfo extends Activity {

    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser=new JSONParser();
    Button nextButton;
    EditText userName;
    EditText userEmail;
    EditText userPhone;
    String url = "http://192.168.1.102:8081/process_post";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        nextButton =(Button) findViewById(R.id.btn_next);
        userName = (EditText)findViewById(R.id.user_name);
        userEmail = (EditText)findViewById(R.id.user_email);
        userPhone = (EditText) findViewById(R.id.user_phone);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm= (ConnectivityManager)UserInfo.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=cm.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                    if (userName.getText().toString().equals("")) {
                        userName.setSelectAllOnFocus(true);
                        userName.requestFocus();
                        Toast.makeText(UserInfo.this, "Name Field is Required", Toast.LENGTH_LONG).show();
                    } else if (userEmail.getText().toString().equals("")) {
                        userEmail.setSelectAllOnFocus(true);
                        userEmail.requestFocus();
                        Toast.makeText(UserInfo.this, "Email Field is Required", Toast.LENGTH_LONG).show();
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail.getText().toString()).matches()) {
                        userEmail.requestFocus();
                        Toast.makeText(UserInfo.this, "Invalid Email ID", Toast.LENGTH_LONG).show();
                    } else if (userPhone.getText().toString().equals("")) {
                        userPhone.setSelectAllOnFocus(true);
                        userPhone.requestFocus();
                        Toast.makeText(UserInfo.this, "Phone Field Required", Toast.LENGTH_LONG).show();
                    }
                else if(!Patterns.PHONE.matcher(userPhone.getText().toString()).matches())
                {
                    userPhone.requestFocus();
                    Toast.makeText(UserInfo.this,"Invalid Phone Number",Toast.LENGTH_LONG).show();
                }
                    else {
                        new InsertUserInfo().execute();
                    }
                }
                else
                {
                    Toast.makeText(UserInfo.this,"No Internet Present",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    class InsertUserInfo extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(UserInfo.this);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("uname",userName.getText().toString()));
            list.add(new BasicNameValuePair("email",userEmail.getText().toString()));
            list.add(new BasicNameValuePair("phone",userPhone.getText().toString()));

            JSONObject obj=jsonParser.makeHttpRequest(url,"GET",list);

            if(obj== null)
            {
                Toast.makeText(UserInfo.this,"Could not Connect to server please try after some time",Toast.LENGTH_LONG).show();
                return null;
            }

            Log.d("Response:",obj.toString());

            try
            {
                int success=obj.getInt(TAG_SUCCESS);

                if(success == 1)
                {
                    SharedPreferences preferences=getApplicationContext().getSharedPreferences("MOGPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("userinfo_executed",true);
                    editor.commit();

                    Intent intent=new Intent(UserInfo.this,HomePage.class);
                    startActivity(intent);

                    finish();
                }
                else
                {
                    Toast.makeText(UserInfo.this,"User Info Didnt insert"+obj.getString("msg"),Toast.LENGTH_LONG);
                }
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }
}
