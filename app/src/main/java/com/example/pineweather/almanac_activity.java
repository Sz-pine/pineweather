package com.example.pineweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pineweather.gson.Almanac;
import com.example.pineweather.util.HttpUtil;
import com.example.pineweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class almanac_activity extends AppCompatActivity {
    public TextView gongliText,nongliText,jiText,yiText,ganzhiText,
                      jieqiText,chongshaText, pengzuText,taishenText;
    public Button back;
    public String TAG="123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almanac_activity);
        gongliText=(TextView)findViewById(R.id.gongli_text);
        nongliText=(TextView)findViewById(R.id.text_nongli);
        jiText=(TextView)findViewById(R.id.text_ji);
        yiText=(TextView)findViewById(R.id.text_yi);
        ganzhiText=(TextView)findViewById(R.id.text_ganzhi);
        jieqiText=(TextView)findViewById(R.id.text_jieqi);
        chongshaText=(TextView)findViewById(R.id.text_chongsha);
        pengzuText=(TextView)findViewById(R.id.text_pengzu);
        taishenText=(TextView)findViewById(R.id.text_taishen);
        back=(Button)findViewById(R.id.back_button);
        Log.d(TAG, "onFailure:333333");
        requestAlmanac();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(almanac_activity.this,WeatherActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showAlmanac(final Almanac almanac) {
        runOnUiThread(new Runnable() {
            @Override
    public void run() {
        String gongli_long=almanac.gongli;
        StringBuffer gongli=new StringBuffer(gongli_long);
        gongli.replace(0,3,"");
        gongliText.setText(gongli.toString());
        String nongli_long=almanac.nongli;
        StringBuffer nongli=new StringBuffer(nongli_long);
        nongli.replace(0,5,"");
        nongliText.setText(nongli.toString());
        String ji=almanac.ji;
        jiText.setText(ji);
        String yi=almanac.yi;
        yiText.setText(yi);
        String ganzhi=almanac.ganzhi;
        ganzhiText.setText(ganzhi);

        String jieqi=almanac.jieqi24;
        jieqiText.setText(jieqi);

        String chongsha=almanac.chongsha;
        chongshaText.setText(chongsha);

        String pzbj=almanac.pzbj;
        pengzuText.setText(pzbj);

        String taishen=almanac.tszf;
        taishenText.setText(taishen);
        Log.d(TAG, "showAlmanac: 55555555");
    }
 });
    }


    public void requestAlmanac() {
        String almanacUrl = "http://route.showapi.com/856-1?showapi_appid=62664&showapi_sign=9794ee3ccaac41e593ebb0a8c179fd98";
        HttpUtil.sendOkHttpRequest(almanacUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure:1111111 ");
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(almanac_activity.this, "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                Log.d(TAG, responseText);
                final Almanac almanac = Utility.handleAlmanacResponse(responseText);
                Log.d(TAG, "onFailure:222222222");
                showAlmanac(almanac);
            }
        });
    }

}
