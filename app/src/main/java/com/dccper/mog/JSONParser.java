package com.dccper.mog;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by reuben.pinto2k15 on 1/13/2016.
 */
public class JSONParser {

    static InputStream is;
    static JSONObject jsonObject=null;

    public JSONParser() {
    }

    public JSONObject makeHttpRequest(String url,String method,List<NameValuePair> param) {
        try {
            if (method.equals("POST"))
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                if(param != null)
                    httpPost.setEntity(new UrlEncodedFormEntity(param));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
            else if(method.equals("GET"))
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                if(param != null) {
                    String paramString = URLEncodedUtils.format(param, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet=new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            if(is==null)
            {
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder stringBuilder=new StringBuilder();
            String line = null;
            while((line = br.readLine()) != null)
            {
                stringBuilder.append(line+"\n");
            }
            is.close();
            jsonObject=new JSONObject(stringBuilder.toString());

        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            Log.e("Error Parsing Data",e.toString());
        }
        return jsonObject;
    }
}
