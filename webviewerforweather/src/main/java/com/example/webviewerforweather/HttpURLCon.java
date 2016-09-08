package com.example.webviewerforweather;

import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LeiYang on 2016/9/5 0005.
 */

public class HttpURLCon {


    public void sendRequestWithHttpURLConnection(final String address,final HttpCallBackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(10*1000);
                    httpURLConnection.setConnectTimeout(10*1000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    Log.i("info","WeatherInfo: " + stringBuilder.toString());
                    if (listener != null) {
                        listener.onFinish(stringBuilder.toString());
                    }
                } catch (MalformedURLException e) {
                    listener.onError(e);
                    e.printStackTrace();
                } catch (IOException e) {
                    listener.onError(e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
