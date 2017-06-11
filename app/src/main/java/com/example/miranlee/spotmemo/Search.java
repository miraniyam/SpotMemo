package com.example.miranlee.spotmemo;

import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,PlaceSelectionListener,OnMapReadyCallback {

    Button start_search;
    GoogleApiClient googleApiClient = null;
    LocationManager locationManager;
    //LocationListener locationListener;
    LocationRequest locationRequest;
    Location location;

    Intent i;
    SpeechRecognizer recognizer;
    boolean SSTsted = false;

    PlaceAutocompleteFragment autocompleteFragment;

    GoogleMap map;

    CharSequence loc_name;


    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        start_search = (Button)findViewById(R.id.start_search);

        autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_search);
        autocompleteFragment.setOnPlaceSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.googlemap);
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

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(listner);


        start_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SSTsted == false){
                    recognizer.startListening(i);
                    //Toast.makeText(view.getContext(),"시작",Toast.LENGTH_LONG).show();
                    SSTsted = true;
                }
                else{
                    recognizer.stopListening();
                    SSTsted = false;
                }
            }
        });
    }
    private RecognitionListener listner = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {

        }

        @Override
        public void onResults(Bundle bundle) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mresult = bundle.getStringArrayList(key);
            String[] rs = new String[mresult.size()];
            mresult.toArray(rs);

            autocompleteFragment.setText(""+rs[0]);

        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };
    @Override
    public void onPlaceSelected(Place place) {
        //장소가 선택 될 때마다 리스트에 주변 메모 보여주기

//        mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
//                place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri(),place.getLatLng()));
//
//        CharSequence attributions = place.getAttributions();
//        if (!TextUtils.isEmpty(attributions)) {
//            mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
//        } else {
//            mPlaceAttribution.setText("");
//        }
        //사용 예시
        updateMap(place);
    }

    @Override
    public void onError(Status status) {
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void updateMap(Place place) {

        map.clear();

        final LatLng Loc = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Loc, 16));

        MarkerOptions options = new MarkerOptions();
        options.position(Loc);
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        options.title(String.valueOf(loc_name));
        //options.snippet("내 위치");
        map.addMarker(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
