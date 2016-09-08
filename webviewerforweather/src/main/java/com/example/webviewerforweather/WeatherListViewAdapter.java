package com.example.webviewerforweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import static com.example.webviewerforweather.R.id.date;
import static com.example.webviewerforweather.R.id.temperature;
import static com.example.webviewerforweather.R.id.weather;
import static com.example.webviewerforweather.R.id.wind;

/**
 * Created by LeiYang on 2016/9/5 0005.
 */

public class WeatherListViewAdapter extends ArrayAdapter<WeatherInfo>{
    Bitmap bitmap;
    public static final int PIG_PICK = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PIG_PICK) {
                Image_Pick image_pick = (Image_Pick) msg.obj;
                image_pick.imageView.setImageBitmap(image_pick.bitmap);

            }
        }
    };


    private int resourceld;
    public WeatherListViewAdapter(Context context, int resource, List<WeatherInfo> list) {
        super(context, resource,list);
        resourceld = resource;
    }

    /**
     * getView()方法会在每个子项滚到屏幕内的时候会被调用
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //得到当前WeatherInfo实例
        WeatherInfo weatherInfo = getItem(position);
        //加载传入的布局
        View view;
        final ViewHolder viewHolder;
        if (convertView == null ) {

            view = LayoutInflater.from(getContext()).inflate(resourceld,null);
            viewHolder = new ViewHolder();
            viewHolder.image_info = (ImageView) view.findViewById(R.id.image_info);
            viewHolder.weather = (TextView) view.findViewById(weather);
            viewHolder.wind = (TextView) view.findViewById(wind);
            viewHolder.temperature = (TextView) view.findViewById(temperature);
            viewHolder.date = (TextView) view.findViewById(date);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
//        final ImageView image_info = (ImageView) view.findViewById(R.id.image_info);
//        TextView weather = (TextView) view.findViewById(R.id.weather);
//        TextView wind = (TextView) view.findViewById(R.id.wind);
//        TextView temperature = (TextView) view.findViewById(R.id.temperature);
//        TextView date = (TextView) view.findViewById(R.id.date);

        viewHolder.weather.setText(weatherInfo.getWeather());
        viewHolder.wind.setText(weatherInfo.getWind());
        viewHolder.temperature.setText(weatherInfo.getTemperature());
        viewHolder.date.setText(weatherInfo.getDate());

        /*获取当前时间*/
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.i("info","时间：" + hour);

        /*判断是晚上还是白天*/
        String dayOrNight;
        if (8 < hour && hour < 20) {
            dayOrNight = weatherInfo.getDayPictureUrl();
        } else {
            dayOrNight = weatherInfo.getNightPictureUrl();
        }

        /**
         * 获取天气图片
         * 由于onFinish()是在子线程中，不能直接更新UI线程
         */
        new GetBitmap().getBitmapFromInternet(dayOrNight, new HttpCallBackListener() {
            @Override
            public void onFinish(Bitmap bitmap) {
                Image_Pick image_pick = new Image_Pick();
                image_pick.imageView = viewHolder.image_info;
                image_pick.bitmap = bitmap;
                Message message = new Message();
                message.what = PIG_PICK;
                message.obj = image_pick;
                handler.sendMessage(message);

            }

            @Override
            public void onFinish(String response) {

            }

            @Override
            public void onError(Exception e) {
                Log.i("info","加载网络图片失败");
            }
        });

        return view;

    }


    class Image_Pick {
        ImageView imageView;
        Bitmap bitmap;
    }

    class ViewHolder{
        ImageView image_info;
        TextView weather;
        TextView wind;
        TextView temperature;
        TextView date;
    }

}
