package com.example.webviewerforweather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LeiYang on 2016/9/7 0007.
 */

public class GetBitmap {
    public void getBitmapFromInternet(final String address, final HttpCallBackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(10*1000);
                    httpURLConnection.setReadTimeout(10*1000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    if (listener != null ) {
                        listener.onFinish(bitmap);
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
