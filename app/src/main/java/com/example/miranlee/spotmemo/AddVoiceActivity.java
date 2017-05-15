package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by miran lee on 2017-05-14.
 */

public class AddVoiceActivity extends ActionBarActivity implements TextToSpeech.OnInitListener{

    MediaRecorder recorder = new MediaRecorder();
    TextToSpeech tts;

    Button startbtn;
    Button stopbtn;
    Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvoice);
        setTitle("음성메모 추가하기");

        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.KOREA);

        startbtn = (Button)findViewById(R.id.btn_start);
        stopbtn = (Button)findViewById(R.id.btn_stop);
        backbtn = (Button)findViewById(R.id.btn_back);
        stopbtn.setEnabled(false);
    }

    public void onClickStart(View view) {
        tts.speak("이 멘트가 끝나면 녹음이 시작됩니다.",TextToSpeech.QUEUE_FLUSH,null);
        try {
            File nfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SpotMemo/Voice/");

            boolean f = nfile.mkdirs();

            String filename = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(nfile.getAbsolutePath()+"/"+filename+".3gp");
            recorder.prepare();
            while(tts.isSpeaking()) {
                // 안내 음성이 다 끝나고 나야 저장할 것이다!
            }
            recorder.start();
            startbtn.setEnabled(false);
            backbtn.setEnabled(false);
            stopbtn.setEnabled(true);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickStop(View view) {
        recorder.stop();
        recorder.release();
        tts.speak("녹음이 완료되었습니다",TextToSpeech.QUEUE_FLUSH,null);
        startbtn.setEnabled(true);
        stopbtn.setEnabled(false);
        backbtn.setEnabled(true);
    }

    @Override
    public void onInit(int i) {

    }

    public void onBack(View view) {
        Intent i = new Intent(this, AddMemoActivity.class);
        finish();
        startActivity(i);
    }
}
