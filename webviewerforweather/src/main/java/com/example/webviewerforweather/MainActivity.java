package com.example.webviewerforweather;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int WEATHER_INFO = 0;
//    private WebView webView;

    private List<WeatherInfo> weatherInfoLists = new ArrayList<>();
    private ListView weatherListView;
    WeatherListViewAdapter weatherListViewAdapter;

    private Button btn_weather;
    private TextView date;
    private ImageView image_info;
    private TextView weather;
    private TextView wind;
    private TextView temperature;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WEATHER_INFO) {

                WeatherInfo weatherInfo = (WeatherInfo) msg.obj;



                weatherInfoLists.add(weatherInfo);
                weatherListViewAdapter.notifyDataSetChanged();

//                data.setText(weatherInfo.getData());
//                image_info.setImageResource(R.mipmap.ic_launcher);
//                weather.setText(weatherInfo.getWeather());
//                wind.setText(weatherInfo.getWind());
//                temperature.setText(weatherInfo.getTemperature());


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_weather = (Button) findViewById(R.id.btn_getWeather);
        weatherListView = (ListView) findViewById(R.id.weatherList);
//        image_info = (ImageView) findViewById(R.id.image_info);
//        weather = (TextView) findViewById(R.id.weather);
//        wind = (TextView) findViewById(R.id.wind);
//        temperature = (TextView) findViewById(R.id.temperature);
//        date = (TextView) findViewById(R.id.date);


        weatherListViewAdapter = new WeatherListViewAdapter(MainActivity.this,R.layout.weatherlist,weatherInfoLists);

        weatherListView.setAdapter(weatherListViewAdapter);

        btn_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = "http://api.map.baidu.com/telematics/v3/weather?location=长沙&output=json&ak=miyQnscUbOg6eAQpw6SoVzqGIf9MxCWA&mcode=A7:1A:C6:2E:81:4E:D3:35:DA:48:26:D1:B9:84:13:EA:BE:78:FC:8A;com.example.webviewerforweather";
                new HttpURLCon().sendRequestWithHttpURLConnection(address, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        parseJSON(response);

                    }

                    @Override
                    public void onFinish(Bitmap bitmap) {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("info","Something Wrong!!");

                    }
                });


            }
        });








        /*
         *WebView测试
        webView = (WebView) findViewById(R.id.webViewer);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());  //JavaScript对话框
        webView.setWebViewClient(new WebViewClient());  //不使用内置浏览器
        webView.loadUrl("http://www.weather.com.cn/adat/sk/101010100.html");
        webView.setInitialScale(57*4);
        */
    }


    private void parseJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject jsonInfo = jsonArray.getJSONObject(0);
            JSONArray json_weather_data = jsonInfo.getJSONArray("weather_data");
            for (int i = 0; i < json_weather_data.length(); i++) {
                JSONObject info = json_weather_data.getJSONObject(i);
                /** JSON中optString和getString的区别
                 * optString方法会在对应的key中的值不存在的时候返回一个空字符串或者返回你指定的默认值，
                 * 但是getString方法会出现空指针异常的错误。
                 */
                WeatherInfo weatherInfo = new WeatherInfo();
                weatherInfo.setData(info.optString("date"));
                weatherInfo.setDayPictureUrl(info.optString("dayPictureUrl"));
                weatherInfo.setNightPictureUrl(info.optString("nightPictureUrl"));
                weatherInfo.setWeather(info.optString("weather"));
                weatherInfo.setWind(info.optString("wind"));
                weatherInfo.setTemperature(info.optString("temperature"));

                Message message = new Message();
                message.what = WEATHER_INFO;
                message.obj = weatherInfo;
                handler.sendMessage(message);



            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
