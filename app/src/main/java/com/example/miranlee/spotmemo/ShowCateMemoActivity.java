package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.location.Location;
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
import java.util.StringTokenizer;

/**
 * Created by miran lee on 2017-05-14.
 */



public class ShowCateMemoActivity extends AppCompatActivity {
    ArrayList<String> fName;
    ArrayList<String> fPath;
    ArrayList<Integer> fType;
    TextToSpeech tts;
    ListView tlistView;

    double latitude = 37.540389;
    double longitude = 127.06923600000005;
    double tlatitude;
    double tlongitude;
    double tcategory;
    double category;
    String slatitude;
    String slongtitude;
    String scategory;
    int Fileidx;
    String FileName;

    float[] distance = {0};
    String sFileName;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmymemo);
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        fName = new ArrayList<>();
        fPath = new ArrayList<>();
        fType = new ArrayList<>();
        tlistView = (ListView) findViewById(R.id.tlistView);
        String ext = Environment.getExternalStorageState();


        if (title.equals("cafe")) {
            setTitle("카페 정보");
            category = 1;
        } else if (title.equals("food")) {
            setTitle("식당 정보");
            category = 2;
        } else if (title.equals("theater")) {
            setTitle("영화관/공연장 정보");
            category = 3;
        } else if (title.equals("shop")) {
            setTitle("옷가게 정보");
            category = 4;
        } else if (title.equals("public")) {
            setTitle("공공시설 정보");
            category = 5;
        }

        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/SpotMemo/Voice");

            ArrayAdapter<String> filelist;
            if (files.listFiles().length > 0) {
                for (File file : files.listFiles()) {
                    // 음성 메모이면 0
                    sFileName = file.getName();
                    Fileidx = sFileName.lastIndexOf(".");
                    FileName = sFileName.substring(0, Fileidx);//확장자 제거
                    StringTokenizer st = new StringTokenizer(FileName, "-");
                    st.nextToken();
                    slatitude = st.nextToken();
                    if (st.hasMoreTokens()) {
                        slongtitude = st.nextToken();
                        scategory = st.nextToken();
                        tcategory = Double.parseDouble(scategory);

                        if (category == tcategory) {
                        tlatitude = Double.parseDouble(slatitude);
                        tlongitude = Double.parseDouble(slongtitude);
                        Location.distanceBetween(latitude, longitude, tlatitude, tlongitude, distance);
                        if (distance[0] < 300) {
                            fName.add(file.getName());
                            fPath.add(file.getAbsolutePath());
                            fType.add(0);
                        }
                        }
                    }
                }
            }
            files = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/SpotMemo/Text");
            if (files.listFiles().length > 0) {
                for (File file : files.listFiles()) {
                    // 텍스트 메모이면 1
                    sFileName = file.getName();
                    Fileidx = sFileName.lastIndexOf(".");
                    FileName = sFileName.substring(0, Fileidx);//확장자 제거
                    StringTokenizer st = new StringTokenizer(FileName, "-");
                    st.nextToken();
                    slatitude = st.nextToken();
                    if (st.hasMoreTokens()) {
                        slongtitude = st.nextToken();
                        scategory = st.nextToken();
                        tcategory = Double.parseDouble(scategory);
                        if (category == tcategory) {
                            tlatitude = Double.parseDouble(slatitude);
                            tlongitude = Double.parseDouble(slongtitude);
                            Location.distanceBetween(latitude, longitude, tlatitude, tlongitude, distance);
                            if (distance[0] < 300) {
                                fName.add(file.getName());
                                fPath.add(file.getAbsolutePath());
                                fType.add(1);
                            }
                        }
                    }
                }

                tlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (fType.get(position) == 0) {
                            // 음성 파일이면 재생
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            try {
                                mediaPlayer.setDataSource(fPath.get(position));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                mediaPlayer.prepare();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mediaPlayer.start();
                        } else {
                            // 텍스트 파일이면 tts로 읽어주기
                            File file = new File(fPath.get(position));
                            String s = "";
                            char ch;
                            int data;
                            FileReader fr = null;
                            try {
                                fr = new FileReader(file);
                                while ((data = fr.read()) != -1) {
                                    ch = (char) data;
                                    s = s + ch;
                                }
                                fr.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (s != "") {
                                tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                            } else {
                                tts.speak("파일 내용이 없습니다.", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    }
                });
            }

            filelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fName);
            tlistView.setAdapter(filelist);
        }
    }

    public void onBack(View view) {
        Intent i = new Intent(this, OthersMemoActivity.class);
        finish();
        startActivity(i);
    }
}
