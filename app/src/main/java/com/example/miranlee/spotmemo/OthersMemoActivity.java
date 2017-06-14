package com.example.miranlee.spotmemo;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class OthersMemoActivity extends AppCompatActivity implements LocationListener{

    ListView tlistView;
    ArrayList<String> fName;
    ArrayList<String> fPath;
    ArrayList<Integer> fType;
    TextToSpeech tts;

    double latitude = 37.540389;
    double longitude = 127.06923600000005;
    double tlatitude;
    double tlongitude;
    String slatitude;
    String slongtitude;
    int Fileidx;
    String FileName;

    float[] distance = {0};
    String sFileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_othersmemo);
        setTitle("주변에 공유된 메모보기");

        fName = new ArrayList<>();
        fPath = new ArrayList<>();
        fType = new ArrayList<>();
        tlistView = (ListView)findViewById(R.id.tlistView);
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Documents/SpotMemo/Voice");

            ArrayAdapter<String> filelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fName);
            if (files.listFiles().length > 0){
                for (File file:files.listFiles()){
                    // 음성 메모이면 0
                    sFileName = file.getName();
                    Fileidx = sFileName.lastIndexOf(".");
                    FileName = sFileName.substring(0, Fileidx);//확장자 제거
                    StringTokenizer st = new StringTokenizer(FileName, "-");
                    st.nextToken();
                    slatitude = st.nextToken();
                    if(st.hasMoreTokens()) {
                        slongtitude = st.nextToken();
                        tlatitude = Double.parseDouble(slatitude);
                        tlongitude = Double.parseDouble(slongtitude);
                        Location.distanceBetween(latitude, longitude,tlatitude, tlongitude, distance);
                        Log.i("현재위도경도",""+latitude+" "+longitude);
                        Log.i("대상위도경도",""+tlatitude+" "+tlongitude);
                        Log.i("거리",""+distance[0]);
                        if (distance[0] < 300) {
                            fName.add(file.getName());
                            fPath.add(file.getAbsolutePath());
                            fType.add(0);
                        }
                    }
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
            tlistView.setAdapter(filelist);

            tlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                            tts.speak(s, TextToSpeech.QUEUE_FLUSH,null);
                        }else {
                            tts.speak("파일 내용이 없습니다.",TextToSpeech.QUEUE_FLUSH,null);
                        }
                    }
                }
            });
        }
    }

    // 카페, 식당, 영화관/공연장, 옷가게, 공공기관

    public void onClickCafe(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","cafe");
        startActivity(i);
    }

    public void onClickFood(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","food");
        startActivity(i);
    }

    public void onClickTheater(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","theater");
        startActivity(i);
    }

    public void onClickShop(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","shop");
        startActivity(i);
    }

    public void onClickPublic(View view) {
        Intent i = new Intent(this, ShowCateMemoActivity.class);
        i.putExtra("title","public");
        startActivity(i);
    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
