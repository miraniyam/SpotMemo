package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by miran lee on 2017-05-14.
 */

public class AddMemoActivity extends AppCompatActivity {
    CharSequence place_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmemo);
        setTitle("메모 추가");
        Intent intent = getIntent();
        place_name =  intent.getCharSequenceExtra("place_name");

    }

    public void onClickAddVoiceMemo(View view) {
        Intent i = new Intent(this, AddVoiceActivity.class);
        i.putExtra("place_name",place_name);
        startActivity(i);
    }

    public void onClickAddTextMemo(View view) {
        Intent i = new Intent(this, AddTextActivity.class);
        i.putExtra("place_name",place_name);
        startActivity(i);
    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }
}
