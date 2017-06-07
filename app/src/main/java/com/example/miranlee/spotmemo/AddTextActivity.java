package com.example.miranlee.spotmemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by miran lee on 2017-05-14.
 */

public class AddTextActivity extends ActionBarActivity implements LocationListener, TextToSpeech.OnInitListener {

    EditText Text;
    private LocationManager locationManager;
    String filename;
    TextToSpeech tts;
    String nar = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);
        setTitle("텍스트 메모 추가하기");

        Text = (EditText)findViewById(R.id.TextMemo);
        tts = new TextToSpeech(this,this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, (LocationListener) this);

    }

    public void onBack(View view) {
        Intent i = new Intent(this, AddMemoActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location) {
        filename = ""+location.getLatitude()+" "+location.getLongitude();
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

    public void onSaveTextMemo(View v) {
        if(Text.getText().toString() == null) {
            nar = "메모 내용을 입력하세요";
            onInit(0);
        }else {
            File nfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SpotMemo/Text/");

            boolean f = nfile.mkdirs();

            File file = new File(nfile.getAbsolutePath() + "/" + filename + ".txt");
            int i = 1;
            while(file.exists()) {
                file = new File(nfile.getAbsolutePath()+"/"+filename+"("+i+").txt");
            }

            FileWriter fw = null;
            BufferedWriter buf = null;

            try {
                fw = new FileWriter(file);
                buf = new BufferedWriter(fw);
                buf.write(Text.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if(buf != null) {
                    buf.close();
                    nar = "저장이 완료되었습니다. 메인으로 돌아갑니다.";
                    onInit(0);
                    startActivity(new Intent(this, MainActivity.class));
                    while(tts.isSpeaking()) {

                    }
                    finish();
                }
                if(fw != null) {
                    fw.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onInit(int status) {
        tts.speak(nar,TextToSpeech.QUEUE_FLUSH,null);
    }
}
