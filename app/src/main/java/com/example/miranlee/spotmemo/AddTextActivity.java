package com.example.miranlee.spotmemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by miran lee on 2017-05-14.
 */

public class AddTextActivity extends ActionBarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);
        setTitle("텍스트 메모 추가하기");
    }
}
