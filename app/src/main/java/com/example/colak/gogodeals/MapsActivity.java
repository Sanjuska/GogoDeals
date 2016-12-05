package com.example.colak.gogodeals;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colak.gogodeals.Popups.DealsPopup;
import com.example.colak.gogodeals.Popups.OptionsPopup;
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

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {



    public static GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;

    Marker mPositionMarker;

    Marker lastOpened = null;

    /*CheckBox food;
    CheckBox clothes;
    CheckBox activities;
    CheckBox stuff;
    CheckBox random;*/

    Location lastFetched;
    static ArrayList<String> filterList;


    boolean isClickedPop = true;
    public static ProgressDialog mProgressDlg;

    static PopupWindow popupMessage;
    static PopupWindow popupDealView;
    Button grabButton;
    Button ungrabButton;
    public static ImageView grabbedView;
    LocationRequest locationRequest;
    public static String descriptionOfGrabbedDeal;
    public static Deal grabbedDeal;


    PopupWindow myDealsPopup;
    ArrayAdapter<Deal> dealAdapter;
    public static List<Deal> dealArrayList;
    static ListView dealListView;

    PopupWindow profilePopup;
    PopupWindow optionsPopup;
    boolean fetched;
    ImageButton optionsButton;
    public static Marker currentMarker;

    LinearLayout mainLayout;

    // Creating an instance of MarkerOptions to set position
    private GoogleApiClient client;

    FilterHandler filterHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
        filterList = new ArrayList<>();
        popupMessage = new PopupWindow(this);
        optionsPopup = new PopupWindow(this);
        profilePopup = new PopupWindow(this);
        popupDealView = new PopupWindow(this);
        myDealsPopup = new PopupWindow(this);

        mainLayout = new LinearLayout(this);
        filterList = new ArrayList<>();

        //create list adapter for deal list
        dealArrayList = new ArrayList<Deal>();
        dealArrayList.add(new Deal());



        // Acquire a reference to the system Location Manager
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);

        SetoptionsButton();



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


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

        // Add a marker in Gothenburg and move the camera

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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



    public void fetchDeals(String filter) {

        ConnectionMqtt connectionMqtt = new ConnectionMqtt(this);

        String subscribeTopic = "deal/gogodeals/database/deals";

                String payload =   "{ \"id\": \"12345678-1011-M012-N210-112233445566\"," +
                        " \"data\": {" +
                        " \"longitude\": " + mLastLocation.getLongitude() + "," +
                        " \"latitude\": " + mLastLocation.getLatitude() + "," +
                        " \"filters\": \""+filter+"\"}}";

                String publishTopic = "deal/gogodeals/deal/fetch";

                Log.i("json publish ",payload);
                connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);

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
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeUseOfNewLocation(Location location) {
        // Define a listener that responds to location updates
        LatLng myLatLang = new LatLng(location.getLatitude(), location.getLongitude());
        // Called when a new location is found by the network location provider.
        CameraPosition myPosition = new CameraPosition.Builder().
                target(myLatLang).zoom(mMap.getCameraPosition().zoom).bearing(mMap.getCameraPosition().bearing).build();
        //locationListener.onLocationChanged(location);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));



        if (!fetched) {


                if (!fetched) {
                    for (String filter : filterList) {
                        fetchDeals(filter);
                        Log.i("json filter ", filter);
                    }
                    fetched = true;
                } else if (lastFetched != null &&
                        mLastLocation != null &&
                        lastFetched.getLatitude() + 0.2 < mLastLocation.getLatitude() &&
                        lastFetched.getLatitude() - 0.2 > mLastLocation.getLatitude() &&
                        lastFetched.getLongitude() + 0.2 < mLastLocation.getLongitude() &&
                        lastFetched.getLongitude() - 0.2 > mLastLocation.getLongitude()) {
                    for (String filter : filterList) {
                        fetchDeals(filter);
                    }
                    lastFetched = mLastLocation;
                }


                mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(myLatLang, myLatLang));
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
        }

        public void animateMarker ( final Marker marker, final Location location){
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



            public void buttonPressed(View v) {

                ConnectionMqtt connectionMqtt = new ConnectionMqtt(this);

                String subscribeTopic = "deal/gogodeals/database/info";

                grabButton = ((Button) v.findViewById(R.id.grabButton));
                grabButton.setVisibility(View.INVISIBLE);

                FrameLayout parentLayout = (FrameLayout) grabButton.getParent();
                LinearLayout grandParentLayout = (LinearLayout) parentLayout.getParent();
                GridLayout popUpLayout = (GridLayout) grandParentLayout.getParent();

                //extract deal id
                TextView idTV = ((TextView) grandParentLayout.findViewById(R.id.idTextView));
                String deal_id = (String) idTV.getText();

                //TODO replace the fix user_id with the user_id currently active in the app
                String payload =   "{ \"id\":\"" + deal_id + "\"," +
                        " \"data\": {" +
                       // " \"user_id\":\"feb00c2b-b4b6-11e6-862e-080027e93e17\"}}";
                " \"user_id\":\"" + MainActivity.userID + "\"}}";

                String publishTopic = "deal/gogodeals/deal/save";
                connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);

                //extract description of deal, to be stored in grabbed deal list on successful grab
                TextView description = ((TextView) popUpLayout.findViewById(R.id.description));
                descriptionOfGrabbedDeal = (String) description.getText();

                //Deal grabbing
                TextView company = ((TextView) popUpLayout.findViewById(R.id.company));
                TextView duration = ((TextView) popUpLayout.findViewById(R.id.duration));
                TextView price = ((TextView) popUpLayout.findViewById(R.id.price));
                ImageView picture = ((ImageView) popUpLayout.findViewById(R.id.dealPicture));
                //TextView description = ((TextView) popUpLayout.findViewById(R.id.description));

                grabbedDeal = new Deal((String) company.getText(), (String) duration.getText(), (String) price.getText(), picture, (String) description.getText(), deal_id);

                //closePopUpButton = (ImageButton) v.findViewById(R.id.cancelButton);
                mProgressDlg = new ProgressDialog(this);
                mProgressDlg.setMessage("Grabbing deal");
                mProgressDlg.setCancelable(false);
                grabButton.setVisibility(View.INVISIBLE);
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {


                    @Override
                    public void run() {
                        popupMessage.dismiss(); // hide dialog
                    }
                };
                handler.removeCallbacks(runnable); // cancel the running action (the hiding process)
                handler.postDelayed(runnable, 3000); // start a new hiding process that will trigger after 5 seconds
                mProgressDlg.show();
            }

        public void ungrabButtonPressed(View v) {
            ConnectionMqtt connectionMqtt = new ConnectionMqtt(this);
            FrameLayout parentLayout = (FrameLayout) ungrabButton.getParent();
            LinearLayout grandParentLayout = (LinearLayout) parentLayout.getParent();
            GridLayout popUpLayout = (GridLayout) grandParentLayout.getParent();

            //extract deal id
            TextView idTV = ((TextView) grandParentLayout.findViewById(R.id.idTextView));
            String deal_id = (String) idTV.getText();

            String payload =   "{ \"id\":\"" + deal_id + "\"," +
                    " \"data\": {" +
                 //   " \"user_id\":\"feb00c2b-b4b6-11e6-862e-080027e93e17\"}}";
                " \"user_id\":\"" + MainActivity.userID + "\"}}";

            String publishTopic = "deal/gogodeals/deal/remove";
            connectionMqtt.sendMqtt(payload,publishTopic);

            //extract description of deal, to be stored in grabbed deal list on successful grab
            TextView description = ((TextView) popUpLayout.findViewById(R.id.description));
            MapsActivity.dealArrayList.remove(grabbedDeal);
            popupDealView.dismiss();

            Toast toast = Toast.makeText(getApplicationContext(), "Deal ungrabbed", Toast.LENGTH_SHORT);
            toast.show();

        }
        }
