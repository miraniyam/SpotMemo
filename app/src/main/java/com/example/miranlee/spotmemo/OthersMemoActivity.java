package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

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

    public void onClickOutside(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","outside");
        startActivity(i);
    }

    public void onClickStore(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","store");
        startActivity(i);
    }

    public void onClickInside(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","inside");
        startActivity(i);
    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }
}
