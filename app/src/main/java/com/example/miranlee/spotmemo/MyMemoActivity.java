package com.example.miranlee.spotmemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by miran lee on 2017-05-14.
 */

public class MyMemoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymemo);
        setTitle("내가 남긴 메모");
    }

    public void onClickOutside(View view) {
        Intent i = new Intent(this, ShowMyMemoActivity.class);
        i.putExtra("title","outside");
        startActivity(i);
    }

    public void onClickStore(View view) {
        Intent i = new Intent(this, ShowMyMemoActivity.class);
        i.putExtra("title","store");
        startActivity(i);
    }

    public void onClickInside(View view) {
        Intent i = new Intent(this, ShowMyMemoActivity.class);
        i.putExtra("title","inside");
        startActivity(i);
    }
}
