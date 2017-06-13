package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileReader;

/**
 * Created by miran lee on 2017-06-13.
 */

public class ReadMemoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    int type;
    String path;
    File f;
    TextView tv;
    TextToSpeech tts;
    boolean isFirst = true;
    String s = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readmemo);
        Intent i = getIntent();
        type = i.getIntExtra("type",-1);
        path = i.getStringExtra("file");

        f = new File(path);
        setTitle(f.getName()+"의 내용");

        tv = (TextView)findViewById(R.id.content);
        tts = new TextToSpeech(this, this);

        if(type == 1) {
            char ch;
            int data;
            FileReader fr = null;
            try {
                fr = new FileReader(f);
                while((data = fr.read()) != -1) {
                    ch = (char)data;
                    s = s+ch;
                }
                fr.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(s != "") {
                tv.setText(s);
            }
        }

    }

    public void onClickMemoListen(View v) {
        if(type == 0) {
            // 음성 파일이면 재생
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }else if(type == 1) {
            // 텍스트 파일이면 tts로 읽어주기
            if(s != "") {
                tts.speak(s, TextToSpeech.QUEUE_FLUSH,null);
            }else {
                tts.speak("파일 내용이 없습니다.",TextToSpeech.QUEUE_FLUSH,null);
            }
        }else {
            tts.speak("파일이 제대로 불려지지 않았습니다.",TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    public void onClickDelete(View v) {
        if(isFirst) {
            tts.speak(f.getName()+"을 정말로 삭제하시겠습니까? 삭제를 원하시면 더블 탭 하세요.",TextToSpeech.QUEUE_FLUSH,null);
            isFirst = false;
        }else {
            isFirst = true;
            tts.speak(f.getName()+"을 삭제합니다.",TextToSpeech.QUEUE_FLUSH,null);
            f.delete();
        }
    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onInit(int status) {

    }
}
