package com.example.colak.gogodeals.MqttModule;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.colak.gogodeals.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Olle, map needs to be initialized :D
        //also changed the version of google play services on gradle.app from 9.6.1 to
        //7.5.0 cause of compatibility.
        //Also, on manifest we just need one main launcher for the whole app :D
        MapsInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Gothenburg and move the camera
        LatLng gothenburg = new LatLng(57.7089, 11.9746);
        mMap.addMarker(new MarkerOptions().position(gothenburg).title("Marker in Gothenburg"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gothenburg));
    }
}
