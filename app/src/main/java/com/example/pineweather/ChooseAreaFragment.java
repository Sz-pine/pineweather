package com.example.pineweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pineweather.db.City;
import com.example.pineweather.db.County;
import com.example.pineweather.db.Province;
import com.example.pineweather.gson.Basic;
import com.example.pineweather.gson.Basic6;
import com.example.pineweather.gson.Weather;
import com.example.pineweather.gson.Weather6;
import com.example.pineweather.util.HttpUtil;
import com.example.pineweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends Fragment {
    public  static final int LEVEL_PROVINCE=0;
    public  static final int LEVEL_CITY=1;
    public  static final int LEVEL_COUNTY=2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList =new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.choose_area,container,false);
       titleText=(TextView) view.findViewById(R.id.title_text);
       backButton=(Button)view.findViewById(R.id.back_button);
       listView=(ListView)view.findViewById(R.id.list_view);
       adapter =new ArrayAdapter<String>(
               getContext(),android.R.layout.simple_list_item_1,dataList);
       listView.setAdapter(adapter);
       return view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryProvince();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCity();
                }else if( currentLevel== LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCounty();
                }else if(currentLevel==LEVEL_COUNTY){
                    String weatherId =countyList.get(position).getWeatherId();
                    Intent intent=new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentLevel==LEVEL_COUNTY){
                    queryCity();
                }else if(currentLevel==LEVEL_CITY){
                    queryProvince();
                }
            }
        });

    }
    private void queryProvince(){
       titleText.setText("中国");
       backButton.setVisibility(View.GONE);
       provinceList= DataSupport.findAll(Province.class);
       if(provinceList.size()>0){
           dataList.clear();
           for (Province province:provinceList) {
               dataList.add(province.getProvinceName());
           }
           adapter.notifyDataSetChanged();
           listView.setSelection(0);
           currentLevel=LEVEL_PROVINCE;
       }else {
           String address ="http://guolin.tech/api/china";
           queryFromServer(address,"province");
       }

    }
    private void queryCity(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid=?",
                String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city:cityList){dataList.add(city.getCityName());}
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else{
            int provinceCode=selectedProvince.getProvinceCode();
            String address="http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }
    private void queryCounty() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid=?",
                String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            String cityName = selectedCity.getCityName();
            String address="https://search.heweather.com/find?"+"location="+cityName+"&key=2f59ea21b7a74bc988ce234528b9e2a6";
            queryFromServer(address, "county");

        }
    }
    private void queryFromServer(String address,final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
             final String responseText=response.body().string();
            boolean result=false;
            if("province".equals(type)){
                result= Utility.handleProvinceResponse(responseText);
            }else if("city".equals(type)){
                result= Utility.handleCityResponse(responseText,selectedProvince.getId());
            }else if("county".equals(type)){
                final Weather6 weather6=Utility.handleCountyResponse(responseText);
                if(weather6!=null){
                for(Basic6 basic6 : weather6.basic6list){
                    County county= new County();
                    county.setCountyName(basic6.location);
                    county.setWeatherId(basic6.cid);
                    county.setCityId(selectedCity.getId());
                    county.save();
                }}
                if(weather6.status.equals("ok")){result=true;}
            }
            if(result){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if("province".equals(type)){queryProvince();}
                        else if("city".equals(type)){queryCity();}
                        else if("county".equals(type)){queryCounty();
                        }
                    }
                });
            }
        }
            @Override
            public void onFailure(Call call, IOException e) {
             e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();

                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void showProgressDialog() {
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }progressDialog.show();
    }
    private void closeProgressDialog() {
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
