package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener{

    Button btn_myMemo;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("스팟메모");

        btn_myMemo = (Button)findViewById(R.id.btn_myMemo);
        // 메모가 몇개인지 알아내서 버튼 이름 바꿔야지~
        tts = new TextToSpeech(this, this);
        tts.speak("주변에 내가 남긴 N개의 메모가 있습니다",TextToSpeech.QUEUE_FLUSH,null);
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

    @Override
    public void onInit(int i) {

    }
}
