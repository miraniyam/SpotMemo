package com.example.miranlee.spotmemo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by miran lee on 2017-05-14.
 */

public class OthersMemoActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othersmemo);
        setTitle("내 주변 메모보기");
    }
}
