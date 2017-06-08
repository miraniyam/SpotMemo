package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by miran lee on 2017-05-14.
 */

public class ShowCateMemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmymemo);
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        if(title.equals("cafe")) {
            setTitle("카페 정보");
        }else if(title.equals("food")) {
            setTitle("식당 정보");
        }else if(title.equals("theater")){
            setTitle("영화관/공연장 정보");
        }else if(title.equals("shop")) {
            setTitle("옷가게 정보");
        }else if(title.equals("public")) {
            setTitle("공공시설 정보");
        }
    }

    public void onBack(View view) {
        Intent i = new Intent(this, OthersMemoActivity.class);
        finish();
        startActivity(i);
    }
}
