package com.example.pineweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.pineweather.db.City;
import com.example.pineweather.db.County;
import com.example.pineweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 刘松松 on 2018/4/16.
 */

public class Utility {
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvinces=new JSONArray(response);
                for(int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject= allProvinces.getJSONObject(i);
                    Province province= new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public  static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities=new JSONArray(response);
                for(int i=0;i<allCities.length();i++){
                    JSONObject cityObject= allCities.getJSONObject(i);
                    City city= new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
           // Log.d("111111111", "handleCountyResponse: 1111111111");
            try {//JSONObject jsonObject=new JSONObject(response);
                //JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
               // String hwContext=jsonArray.getJSONObject(0).toString();
                //Log.d(hwContext, "hwContext 222222222");
                JSONArray allCounty=new JSONArray(response);
               // JSONObject jsonObject1=new JSONObject(hwContext);
                //JSONArray jsonArray1=jsonObject1.getJSONArray("basic");
                //String basicContext=jsonArray1.getJSONObject(0).toString();
               // Log.d(basicContext, "basicContext: 333333333");
                //System.out.println(basicContext);
                //JSONArray jsonArray2=new JSONArray(basicContext);
                //JSONObject jsonObject2 = new JSONObject(basicContext);
                //JSONArray jsonArray2=jsonObject2.optJSONArray("basic");
                //JSONArray allCounties=new JSONArray(response);
                for(int i=0;i<allCounty.length();i++){
                    JSONObject countyObject= allCounty.getJSONObject(i);
                    County county= new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
