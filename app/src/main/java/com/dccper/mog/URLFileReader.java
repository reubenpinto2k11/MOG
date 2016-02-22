package com.dccper.mog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by reuben.pinto2k15 on 1/25/2016.
 */
public class URLFileReader {
    private InputStream is;
    private String data;

    public URLFileReader() {
    }

    public String makeHttpRequest(String url,String method){
        try {
            if (method.equals("POST"))
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
            else if(method.equals("GET"))
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet=new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        StringBuilder stringBuilder=new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while((line = br.readLine()) != null)
            {
                stringBuilder.append(line+"\n");
            }
            is.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
