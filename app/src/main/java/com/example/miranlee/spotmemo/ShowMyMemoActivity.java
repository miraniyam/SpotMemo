package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by miran lee on 2017-05-14.
 */

public class ShowMyMemoActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        if(title.equals("outside")) {
            setTitle("도로/거리 정보");
        }else if(title.equals("store")) {
            setTitle("상점/맛집 정보");
        }else {
            setTitle("실내/건물 정보");
        }
    }
}
