package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by miran lee on 2017-05-14.
 */

public class MyMemoActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> fName;
    ArrayList<String> fPath;
    ArrayList<Integer> fType;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymemo);
        setTitle("내가 남긴 메모");

        tts = new TextToSpeech(this,this);

        fName = new ArrayList<>();
        fPath = new ArrayList<>();
        fType = new ArrayList<>();
        listView = (ListView)findViewById(R.id.mlistView);

        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Documents/SpotMemo/Voice");
            String numfile[] = files.list();

            ArrayAdapter<String> filelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fName);
            if (files.listFiles().length > 0){
                for (File file:files.listFiles()){
                    // 음성 메모이면 0
                    fName.add(file.getName());
                    fPath.add(file.getAbsolutePath());
                    fType.add(0);
                }
            }
            files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Documents/SpotMemo/Text");
            if (files.listFiles().length > 0){
                for (File file:files.listFiles()){
                    // 텍스트 메모이면 1
                    fName.add(file.getName());
                    fPath.add(file.getAbsolutePath());
                    fType.add(1);
                }
            }
            listView.setAdapter(filelist);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(fType.get(position) == 0) {
                        // 음성 파일이면 재생
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(fPath.get(position));
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        try{
                            mediaPlayer.prepare();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                    }else {
                        // 텍스트 파일이면 tts로 읽어주기
                        File file = new File(fPath.get(position));
                        String s = "";
                        char ch;
                        int data;
                        FileReader fr = null;
                        try {
                            fr = new FileReader(file);
                            while((data = fr.read()) != -1) {
                                ch = (char)data;
                                s = s+ch;
                            }
                            fr.close();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(s != "") {
                            tts.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                        }else {
                            tts.speak("파일 내용이 없습니다.",TextToSpeech.QUEUE_FLUSH,null);
                        }
                    }
                }
            });
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
