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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by miran lee on 2017-05-14.
 */

public class AddTextActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, TextToSpeech.OnInitListener, LocationListener, AdapterView.OnItemClickListener {

    EditText Text;
    private LocationManager locationManager;
    String filename;
    TextToSpeech tts;
    String nar = null;


    TextView getPlace;

    GoogleApiClient googleApiClient = null;

    //LocationListener locationListener;
    LocationRequest locationRequest;
    Location location;

    ListView listView;
    ArrayList<Place_Info> place_name;
    Place_Adapter aa;

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);
        setTitle("텍스트 메모 추가하기");

        Text = (EditText)findViewById(R.id.TextMemo);
        tts = new TextToSpeech(this,this);


        getPlace = (TextView) findViewById(R.id.getPlace);
        listView = (ListView)findViewById(R.id.listview);

        place_name = new ArrayList<Place_Info>();

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
                    Place_Info place_info = new Place_Info( placeLikelihood.getPlace().getName(),placeLikelihood.getPlace().getLatLng());
                    place_name.add( place_info);


                }
                likelyPlaces.release();
            }
        });

        aa = new Place_Adapter(this,R.layout.place_row, place_name);

        listView.setAdapter(aa);
        listView.setOnItemClickListener(this);


    }

    public void onBack(View view) {
        Intent i = new Intent(this, AddMemoActivity.class);
        finish();
        startActivity(i);
    }

    @Override
    public void onLocationChanged(Location location) {
        filename = ""+location.getLatitude()+" "+location.getLongitude();
        getPlace.setText(""+location.getLatitude()+" "+location.getLongitude());
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
                file = new File(nfile.getAbsolutePath()+"/"+filename+"("+(i++)+").txt");
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
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);

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
            getPlace.setText(place_name.get(i).place_name + "-" + place_name.get(i).place_latlng.latitude + "-" + place_name.get(i).place_latlng.longitude);

            listView.setAdapter(aa);
        }
    }
}
