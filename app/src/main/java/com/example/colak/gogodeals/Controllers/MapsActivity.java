package com.example.colak.gogodeals.Controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import com.example.colak.gogodeals.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/* This class is the main class where the map is showed, the same as deals, location and the Options button.*/

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    static Marker mPositionMarker;
    public static Activity mapsActivity;
    Marker lastOpened = null;
    Location lastFetched;
    public static boolean firstLoad;
    LocationRequest locationRequest;
    static LatLng myLatLang;

    boolean fetched;
    ImageButton optionsButton;
    public static Marker currentMarker;
    // Creating an instance of MarkerOptions to set position
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapsActivity = this;

        firstLoad = true;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();



        //also changed the version of google play services on gradle.app from 9.6.1 to
        //7.5.0 cause of compatibility.
        MapsInitializer.initialize(getApplicationContext());

        //Sanja && Johan
        //Show my location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        fetched = false;

        // Acquire a reference to the system Location Manager
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
        SetoptionsButton();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        MainActivity.messages.getGrabbedDeals();
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.whitegrey));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        // Current users location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //GoogleMap settings
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMinZoomPreference(16.0f);
        mMap.setMaxZoomPreference(19.0f);

        // GoogleMap marker settings
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    boolean doNotMoveCameraToCenterMarker = true;
                    public boolean onMarkerClick(Marker marker) {
                        currentMarker = marker;
                        if (marker.getTitle().equals("user")) {
                        } else {
                            // Check if there is an open info window
                            if (lastOpened != null) {
                                // Close the info window
                                lastOpened.hideInfoWindow();
                                // Is the marker the same marker that was already open
                                if (lastOpened.equals(marker)) {
                                    // Nullify the lastOpenned object
                                    lastOpened = null;
                                    // Return so that the info window isn't openned again
                                    return true;
                                }
                            }
                            startActivity(new Intent(MapsActivity.this, DealsPopup.class));
                        }
                        return doNotMoveCameraToCenterMarker;
                    }
                });
    }


    public void SetoptionsButton(){
        optionsButton = (ImageButton) findViewById(R.id.optionslistbutton);
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, OptionsPopup.class));
            }
        });
    }


    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        makeUseOfNewLocation(mLastLocation);
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            makeUseOfNewLocation(location);
        }
    };

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, locationListener);
    }

    //Location view settings
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeUseOfNewLocation(Location location) {
        // Define a listener that responds to location updates
        myLatLang = new LatLng(location.getLatitude(), location.getLongitude());
        // Called when a new location is found by the network location provider.
        CameraPosition myPosition = new CameraPosition.Builder().
                target(myLatLang).zoom(mMap.getCameraPosition().zoom).bearing(mMap.getCameraPosition().bearing).build();
        //locationListener.onLocationChanged(location);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

                if (!fetched) {
                    fetched = true;
                    //MainActivity.messages.getFilters();
                    Log.i("filter ","initial mapsA");
                } else if (lastFetched != null &&
                        mLastLocation != null &&
                        lastFetched.getLatitude() + 0.2 < mLastLocation.getLatitude() &&
                        lastFetched.getLatitude() - 0.2 > mLastLocation.getLatitude() &&
                        lastFetched.getLongitude() + 0.2 < mLastLocation.getLongitude() &&
                        lastFetched.getLongitude() - 0.2 > mLastLocation.getLongitude())
                {
                    Log.i("filter ", "loop mapsA");
                        MainActivity.messages.fetchDeals(MainActivity.filterList,mLastLocation);
                    lastFetched = mLastLocation;
                }

                mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(myLatLang, myLatLang));

                userMarker(location);

        }
    //Create marker icon for the USER
    public static void userMarker(Location location){
        if (mPositionMarker == null) {
            mPositionMarker = mMap.addMarker(new MarkerOptions()
                    .flat(true)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.shopper))
                    .anchor(0.5f, 0.5f)
                    .title("user")
                    .position(myLatLang));
        }
        mPositionMarker.hideInfoWindow();
        animateMarker(mPositionMarker, location); // Helper method for smooth
        // animation
    }


    public static void animateMarker(final Marker marker, final Location location){
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(mMap.getCameraPosition().bearing);
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
        }