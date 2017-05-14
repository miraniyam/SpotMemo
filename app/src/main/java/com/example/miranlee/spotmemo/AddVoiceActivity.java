package com.example.miranlee.spotmemo;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvoice);
        setTitle("음성메모 추가하기");

        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.KOREA);
    }

    public void onClickStart(View view) {
        tts.speak("녹음을 시작합니다",TextToSpeech.QUEUE_FLUSH,null);
        try {
            File nfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SpotMemo/Voice/");

            boolean f = nfile.mkdirs();

            String filename = new SimpleDateFormat("yyyy-mm-dd-hh-mm").format(new Date());

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(nfile.getAbsolutePath()+"/"+filename+".3gp");
            recorder.prepare();
            while(tts.isSpeaking()) {
                // 안내 음성이 다 끝나고 나야 저장할 것이다!
            }
            recorder.start();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickStop(View view) {
        recorder.stop();
        recorder.release();
        tts.speak("녹음이 완료되었습니다",TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    public void onInit(int i) {

    }
}
