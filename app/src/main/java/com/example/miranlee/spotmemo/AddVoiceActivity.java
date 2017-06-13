package com.example.miranlee.spotmemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by miran lee on 2017-05-14.
 */

public class AddVoiceActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, TextToSpeech.OnInitListener, LocationListener, AdapterView.OnItemClickListener {

    private LocationManager locationManager;

    AudioRecorder audioRecorder;
    TextToSpeech tts;

    Button startbtn;
    Button stopbtn;
    Button backbtn;
    Button playbtn;
    Button cancelbtn;

    String msg;

    TextView getPlace;

    GoogleApiClient googleApiClient = null;

    //LocationListener locationListener;
    LocationRequest locationRequest;
    Location location;

    ListView listView;
    ArrayList<CharSequence> place_name;

    boolean recordingState = false;
    boolean newRecording = true;

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvoice);
        setTitle("음성메모 추가하기");


        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.KOREA);

        getPlace = (TextView) findViewById(R.id.getPlace);
        listView = (ListView)findViewById(R.id.listview);

        place_name = new ArrayList<CharSequence>();

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

        startbtn = (Button) findViewById(R.id.btn_start);
        stopbtn = (Button) findViewById(R.id.btn_stop);
        backbtn = (Button) findViewById(R.id.btn_back);
        playbtn = (Button)findViewById(R.id.btn_listen);
        cancelbtn = (Button)findViewById(R.id.btn_cancel);
        stopbtn.setEnabled(false);
        playbtn.setEnabled(false);
        cancelbtn.setEnabled(false);


        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API) //로케이션정보를 얻겠다
                    .build();
        }

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(1000)
                .setSmallestDisplacement(100);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        int Fine_Perm_Check = ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_FINE_LOCATION);

        if(Fine_Perm_Check == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            },0);
        }

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    place_name.add( placeLikelihood.getPlace().getName());
                    getPlace.setText(String.format("Place '%s' has likelihood: %g LatLng : %s ",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood(),
                            placeLikelihood.getPlace().getLatLng()));
                }
                likelyPlaces.release();
            }
        });

        ArrayAdapter<CharSequence> aa = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_dropdown_item_1line,place_name);

        listView.setAdapter(aa);
        listView.setOnItemClickListener(this);


    }

    public void onClickStart(View view) {
        if(!recordingState) {
            // 녹음중이 아니라면 -> 녹음 시작
            recordingState = true;
            try {
                File nfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SpotMemo/Voice/");

                boolean f = nfile.mkdirs();

                if(newRecording) {
                    tts.speak("이 멘트가 끝나면 녹음이 시작됩니다. 다시 더블탭 하면 녹음이 일시정지 되고, 녹음을 취소하거나 저장, 재생할 수 있습니다.", TextToSpeech.QUEUE_FLUSH, null);
                    audioRecorder = AudioRecorderBuilder
                            .with(getApplicationContext())
                            .fileName(nfile.getAbsolutePath() + "/" + getPlace.getText() + ".mp4")
                            .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                            .loggable()
                            .build();
                    newRecording = false;
                }else {
                    tts.speak("이 멘트가 끝나면 녹음이 다시 시작됩니다.",TextToSpeech.QUEUE_FLUSH,null);
                    while (tts.isSpeaking()) {
                        // 안내 음성이 다 끝나고 나야 저장할 것이다!
                    }
                }

                audioRecorder.start(new AudioRecorder.OnStartListener() {
                    @Override
                    public void onStarted() {
                        while (tts.isSpeaking()) {
                            // 안내 음성이 다 끝나고 나야 저장할 것이다!
                        }
                        startbtn.setText("일시 정지");
                        playbtn.setEnabled(false);
                        backbtn.setEnabled(false); // 녹음 중에는 뒤로 가지 못하게
                        stopbtn.setEnabled(false); // 녹음 중에는 저장 할 수 없게
                        cancelbtn.setEnabled(true);
                    }

                    @Override
                    public void onException(Exception e) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            // 녹음중 이었다면 -> 일시정지 상태로
            audioRecorder.pause(new AudioRecorder.OnPauseListener() {
                @Override
                public void onPaused(String activeRecordFileName) {
                    recordingState = false;
                    stopbtn.setEnabled(true); // 그대로 저장할 수 있음
                    tts.speak("녹음이 일시정지 되었습니다.", TextToSpeech.QUEUE_FLUSH, null);
                    startbtn.setText("이어서 녹음");
                    playbtn.setEnabled(true);
                    cancelbtn.setEnabled(true);
                }

                @Override
                public void onException(Exception e) {

                }
            });
        }
    }

    public void onClickStop(View view) { // 녹음 저장
        tts.speak(getPlace.getText()+"이름으로 녹음이 완료되었습니다",TextToSpeech.QUEUE_FLUSH,null);
        newRecording = true;
        recordingState = false;
        startbtn.setEnabled(true);
        startbtn.setText("녹음 시작");
        stopbtn.setEnabled(false);
        backbtn.setEnabled(true);
        cancelbtn.setEnabled(false);
    }

    public void onClickListen(View view) {
        if(!newRecording) {
            recordingState = false;
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioRecorder.getRecordFileName());
            }catch (Exception e) {
                e.printStackTrace();
            }
            try{
                mediaPlayer.prepare();
            }catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
    }

    public void onClickCancel(View v) {
        if(!newRecording) {
            newRecording = true;
            recordingState = false;
            audioRecorder.cancel();
            startbtn.setText("녹음 시작");
            startbtn.setEnabled(true);
            stopbtn.setEnabled(false);
            backbtn.setEnabled(true);
            tts.speak("녹음이 취소되어 기존 내용은 저장되지 않았습니다", TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int i) {

    }

    public void onBack(View view) {
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    public void onLocationChanged(Location location){

        msg = ""+location.getLatitude() +" "+location.getLongitude();

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int Fine_Perm_Check = ContextCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_FINE_LOCATION);

            if(Fine_Perm_Check == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this,new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },0);
            }
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView == listView){
            getPlace.setText(place_name.get(i));
        }
    }
}
