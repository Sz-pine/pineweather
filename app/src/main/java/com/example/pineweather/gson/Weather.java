package com.example.pineweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘松松 on 2018/4/17.
 */

public class Weather {
    public String status;
    public Aqi aqi;
    public Basic basic;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
