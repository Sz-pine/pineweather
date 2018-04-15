package com.example.pineweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘松松 on 2018/4/15.
 */

public class City extends DataSupport {
    private int id;
    private int cityId;
    private int cityCode;
    private String provinceId;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }
}
