package com.example.miranlee.spotmemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by miran lee on 2017-05-14.
 */

public class MyMemoActivity extends ActionBarActivity {

    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymemo);
        setTitle("내가 남긴 메모");

        listView = (ListView)findViewById(R.id.mlistView);
        adapter = new ArrayAdapter(getApplicationContext(), R.layout.row);
        listView.setAdapter(adapter);
    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }
}
