package com.example.pineweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘松松 on 2018/4/15.
 */

public class Province extends DataSupport {
    private int id;
    private int provinceCode;
    private String provinceName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
