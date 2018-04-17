package com.example.pineweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘松松 on 2018/4/15.
 */

public class County extends DataSupport {
    private int cityId;
    private String countyName, weatherId;

  //  public int getId() {
    //    return id;
   // }
    //public void setId(int id){
   //     this.id=id;
   // }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
