package com.example.miranlee.spotmemo;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener, OnMapReadyCallback, TextToSpeech.OnInitListener{

    Button btn_myMemo;
    Button btn_addMemo;
    Button btn_othersMemo;
    TextToSpeech tts;


    GoogleMap map;
    double latitude;
    double longitude;
    String slatitude;
    String slongtitude;
    double tlatitude;
    double tlongitude;
    File filesv;
    File filest;

    int numvoice=0;
    int numtext=0;
    int numoffile = 0;
    int Fileidx;
    String sFileName;
    String FileName;
    GoogleApiClient googleApiClient = null;

    LocationManager locationManager;
    //LocationListener locationListener;
    LocationRequest locationRequest;
    Location location;

    Place_Info place_info;


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("스팟메모");


        int Fine_Perm_Check = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO);

        if(Fine_Perm_Check == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.RECORD_AUDIO
            },0);
        }


        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            filesv = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/SpotMemo/Voice");
            filesv.mkdirs(); // 있으면 안만들거고, 없으면 만들어주게~
            String numfilev[] = filesv.list();
            numvoice = numfilev.length;
            filest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/SpotMemo/Text");
            filest.mkdirs();
            String numfilet[] = filest.list();
            numtext = numfilet.length;
        }
        numoffile = numvoice + numtext;
        btn_myMemo = (Button)findViewById(R.id.btn_myMemo);
        String hey = "내가 남긴 "+String.valueOf(numoffile)+"개 메모";

        btn_myMemo.setText(hey);
        // 메모가 몇개인지 알아내서 버튼 이름 바꿔야지~
        tts = new TextToSpeech(this, this);
        //tts.speak("주변에 내가 남긴 N개의 메모가 있습니다",TextToSpeech.QUEUE_FLUSH,null);


        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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



        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                int i = 0;
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    if(i == 0)
                        place_info = new Place_Info( placeLikelihood.getPlace().getName(),placeLikelihood.getPlace().getLatLng());
                    i++;
                }
                likelyPlaces.release();
            }
        });
    }

    public void changeActivity(View view){
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_myMemo :
                intent = new Intent(this,MyMemoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_addMemo :
                intent = new Intent(this,Choose_Place.class);
                startActivity(intent);
                break;
            case R.id.btn_othersMemo :
                intent = new Intent(this,OthersMemoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_search:
                intent = new Intent(this,Search.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onInit(int i) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int Fine_Perm_Check = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if(Fine_Perm_Check == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(this,new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },0);
            }
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location != null)
            updateMap(location);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {

//뭐 쓴척 하기
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            // Log.i("Location")
            updateMap(location);
        }
    }

    @Override
    public void onPlaceSelected(Place place) {

    }

    @Override
    public void onError(Status status) {
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


    }

    public void updateMap(Location location) {
        map.clear();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        final LatLng Loc = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc, 16));

        MarkerOptions options = new MarkerOptions();
        options.position(Loc);
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        options.title("내 위치");
        if(place_info!=null)
            options.snippet(String.valueOf(place_info.place_name));
        else
            options.snippet("X");

        map.addMarker(options);
        //mylocation.setText(latitude+" , "+longitude);
        if (filesv.listFiles().length > 0) {
                for (File file : filesv.listFiles()) {
                    // 음성 메모이면 0
                    sFileName = file.getName();
                    Fileidx = sFileName.lastIndexOf(".");
                    FileName = sFileName.substring(0, Fileidx);//확장자 제거
                    StringTokenizer st = new StringTokenizer(FileName,"-");
                    st.nextToken();
                    slatitude = st.nextToken();
                    if(st.hasMoreTokens()) {
                        slongtitude = st.nextToken();
                        tlatitude = Double.parseDouble(slatitude);
                        tlongitude = Double.parseDouble(slongtitude);

                        MarkerOptions moptions = new MarkerOptions();
                        final LatLng Locc = new LatLng(tlatitude, tlongitude);
                        moptions.position(Locc);
                        moptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        map.addMarker(moptions);
                    }
                }
        }
    }
}
