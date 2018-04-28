package com.example.pineweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 刘松松 on 2018/4/23.
 */

public class Weather6 {
    public  String status;
    @SerializedName("basic")
    public List<Basic6> basic6list;
}
