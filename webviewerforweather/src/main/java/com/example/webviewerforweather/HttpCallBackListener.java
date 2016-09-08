package com.example.webviewerforweather;

import android.graphics.Bitmap;

/**
 * Created by LeiYang on 2016/9/5 0005.
 */

public interface HttpCallBackListener {
    void onFinish(Bitmap bitmap);
    void onFinish(String response);
    void onError(Exception e);


}
