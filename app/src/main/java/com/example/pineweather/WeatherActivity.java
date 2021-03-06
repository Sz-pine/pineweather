package com.example.pineweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.pineweather.gson.Forecast;
import com.example.pineweather.gson.Weather;
import com.example.pineweather.service.AutoUpdateService;
import com.example.pineweather.util.HttpUtil;
import com.example.pineweather.util.Utility;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherlayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    private Button navButton;
    public String TAG="123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherlayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        String weatherId;
        final String swRweatherid;
        swRweatherid= getIntent().getStringExtra("weather_id");
        weatherId = getIntent().getStringExtra("weather_id");
        weatherlayout.setVisibility(View.INVISIBLE);
        swipeRefresh.setRefreshing(true);
        requestWeather(weatherId);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(swRweatherid);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        navButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    });
        nav_view.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
        drawerLayout.closeDrawers();
        if (item.getItemId()==R.id.nav_location) {
                Intent lacationintent = new Intent(WeatherActivity.this, location_Activity.class);
                startActivity(lacationintent);
                finish();
            } else if(item.getItemId()==R.id.nav_almanac){
                Intent almanacintent = new Intent(WeatherActivity.this, almanac_activity.class);
                startActivity(almanacintent);
            } else if(item.getItemId()==R.id.nav_constellation){
                Log.d(TAG, "onNavigationItemSelected:22222222222 ");
                Intent almanacintent = new Intent(WeatherActivity.this, almanac_activity.class);
                startActivity(almanacintent);

            } else if(item.getItemId()==R.id.nav_about){
                Log.d(TAG, "onNavigationItemSelected:22222222222 ");
                Intent almanacintent = new Intent(WeatherActivity.this, almanac_activity.class);
                startActivity(almanacintent);
        }
        return false;
    }
    });

}
    private void loadBingPic() {
        final String requestBingPic="http:guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl="http://guolin.tech/api/weather?cityid="+
                weatherId+"&key=2f59ea21b7a74bc988ce234528b9e2a6";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(
                                    WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.putString("weather_id",weatherId);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气失败",Toast.LENGTH_SHORT).show();
                        }swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather) {
        String cityName =weather.basic.cityName;
        String updateTime="更新时间:"+weather.basic.update.updateTime.split(" ")[1];
        String degree =weather.now.temperature + "℃";
        String weatherInfo=weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
       for(Forecast forecast : weather.forecastList){
            View view= LayoutInflater.from(this).inflate(
                    R.layout.forecast_item,forecastLayout,false);
            TextView dateText=(TextView)view.findViewById(R.id.date_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max+"℃");
            minText.setText(forecast.temperature.min+"℃");
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            int i = Integer.parseInt(weather.aqi.city.aqi);
            String air = null;
            if(i<51){air="优";}
            if(i>50&&i<101){air="良";}
            if(i>100&&i<151){air="轻度污染";}
            if(i>150&&i<201){air="中度污染";}
            if(i>200&&i<301){air="重度污染";}
            if(i>300){air="严重污染";}
            int j = Integer.parseInt(weather.aqi.city.pm25);
            String pm = null;
            if(j<36){pm="优";}
            if(j>35&&j<76){pm="良";}
            if(j>75&&j<116){pm="轻度污染";}
            if(j>115&&j<151){pm="中度污染";}
            if(j>150&&j<251){pm="重度污染";}
            if(j>250){pm="严重污染";}
            aqiText.setText(weather.aqi.city.aqi+" "+air);
            pm25Text.setText(weather.aqi.city.pm25+" "+pm);
        }
        String comfort="舒适度:"+weather.suggestion.comfort.info;
        String carWash="洗车:"+weather.suggestion.carWash.info;
        String sport="运动指数:"+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        sportText.setText(sport);
        carWashText.setText(carWash);
        weatherlayout.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
