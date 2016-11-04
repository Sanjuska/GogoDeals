package com.example.colak.gogodeals.MqttModule;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.colak.gogodeals.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public Deals deals;

    public static GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;

    LocationManager locationManager;

    Marker lastOpened = null;

    PopupWindow popupMessage;
    LocationRequest locationRequest;


    Button popupButton;
    LinearLayout mainLayout;

    // Creating an instance of MarkerOptions to set position
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupMessage = new PopupWindow(this);

        mainLayout = new LinearLayout(this);
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

        // Create MQTT connection and create listeners to new messages
        deals = new Deals(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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
        // Acquire a reference to the system Location Manager
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
    }

    //Olle, adding Gothenburg marker on the map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Gothenburg and move the camera
        LatLng gothenburg = new LatLng(57.7089, 11.9746);
        mMap.addMarker(new MarkerOptions().position(gothenburg).title("Gothenburg"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(gothenburg));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //GoogleMap settings
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMinZoomPreference(14.0f);
        mMap.setMaxZoomPreference(18.0f);



        // GoogleMap marker settings
        mMap.setOnMarkerClickListener(
                new GoogleMap.OnMarkerClickListener() {
                    boolean doNotMoveCameraToCenterMarker = true;

                    public boolean onMarkerClick(Marker marker) {
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
                        Bitmap icon;

                        //BitmapDescriptor deal = BitmapDescriptorFactory.fromResource(R.drawable.deal);
                        BitmapDescriptor deal = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                        marker.setIcon(deal);


                        View popup = getContent(marker);
                        popupMessage.setContentView(popup);
                        popupMessage.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                        popupMessage.update(700, 620);


                        //marker.showInfoWindow();
                        // Re-assign the last openned such that we can close it later
                        lastOpened = marker;
                        return doNotMoveCameraToCenterMarker;
                    }
                });


    }

    //Unused method
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    public View getContent(Marker marker) {

        // Getting view from the layout file info_window_layout
        View v = getLayoutInflater().inflate(R.layout.deal_pop_up, null);

        String[] components = marker.getSnippet().split(";");

        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(components[0].split(":")[1]);
        Log.d("InfoWindow description:", components[0]);

        TextView price = ((TextView) v.findViewById(R.id.price));
        price.setText(components[1].split(":")[1]);
        Log.d("InfoWindow description:", components[1]);

        TextView units = ((TextView) v.findViewById(R.id.units));
        units.setText(components[2].split(":")[1]);
        Log.d("InfoWindow description:", components[0]);

        //Chronometer duration = ((Chronometer) v.findViewById(R.id.duration));
        //String dur = components[3].split(":")[1];
        //Log.d("InfoWindow description:", dur);

        TextView duration = ((TextView) v.findViewById(R.id.duration));
        duration.setText(components[3].split(":")[0]);
        //Log.d("InfoWindow description:", components[1]);

        Button grab = ((Button) v.findViewById(R.id.grabButton));
        grab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMessage.dismiss();
            }
        });

        ImageView dealPicture = (ImageView) v.findViewById(R.id.dealPicture);
        // Converting String byte picture to an ImageView
        String base = components[4].split(",")[1];
        byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        dealPicture.setImageBitmap(decodedByte);
        Log.d("InfoWindow picture:", components[4]);

        // Returning the view containing InfoWindow contents
        return v;

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            makeUseOfNewLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, locationListener);
    }

    //Location view settings
    private void makeUseOfNewLocation(Location location) {
        // Define a listener that responds to location updates
        LatLng myLatLang = new LatLng(location.getLatitude(), location.getLongitude());
        // Called when a new location is found by the network location provider.
        CameraPosition myPosition = new CameraPosition.Builder().
                target(myLatLang).zoom(17.0f).build();
        //locationListener.onLocationChanged(location);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
    }
}
