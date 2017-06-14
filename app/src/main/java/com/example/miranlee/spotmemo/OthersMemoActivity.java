package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by miran lee on 2017-05-14.
 */

public class OthersMemoActivity extends AppCompatActivity implements LocationListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othersmemo);
        setTitle("주변에 공유된 메모보기");


    }

    // 카페, 식당, 영화관/공연장, 옷가게, 공공기관

    public void onClickCafe(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","cafe");
        startActivity(i);
    }

    public void onClickFood(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","food");
        startActivity(i);
    }

    public void onClickTheater(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","theater");
        startActivity(i);
    }

    public void onClickShop(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","shop");
        startActivity(i);
    }

    public void onClickPublic(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","public");
        startActivity(i);
    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
