package com.example.pineweather.gson;

/**
 * Created by 刘松松 on 2018/4/17.
 */

public class Aqi {
    public AqiCity city;
    public class AqiCity{
        public String aqi;
        public String pm25;
    }
}
