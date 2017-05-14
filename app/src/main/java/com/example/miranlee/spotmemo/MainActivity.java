package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    Button btn_myMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("스팟메모");

        btn_myMemo = (Button)findViewById(R.id.btn_myMemo);
        // 메모가 몇개인지 알아내서 버튼 이름 바꿔야지~
    }

    public void onClickMyMemo(View view) {
        Intent intent = new Intent(this,MyMemoActivity.class);
        startActivity(intent);
    }

    public void onClickAddMemo(View view) {
        Intent intent = new Intent(this,AddMemoActivity.class);
        startActivity(intent);
    }

    public void onClickOthersMemo(View view) {
        Intent intent = new Intent(this,OthersMemoActivity.class);
        startActivity(intent);
    }
}
