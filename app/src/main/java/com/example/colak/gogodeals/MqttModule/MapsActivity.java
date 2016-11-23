package com.example.colak.gogodeals.MqttModule;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ConnectionMqtt dealMqqt;
    public static GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Handler fetchHandler;
    Marker mPositionMarker;
    LatLng lastDealUpdatePosition;
    Marker lastOpened = null;
    boolean isClickedPop = true;
    public static ProgressDialog mProgressDlg;

    PopupWindow popupMessage;
    Button grabButton;
    static ImageView grabbedView;
    ImageButton closePopUpButton;
    LocationRequest locationRequest;
    PopupWindow filterPopup;
    PopupWindow myDealsPopup;
    PopupWindow profilePopup;
    PopupWindow optionsPopup;
    LinearLayout mainLayout;

    // Creating an instance of MarkerOptions to set position
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupMessage = new PopupWindow(this);
        optionsPopup = new PopupWindow(this);
        profilePopup = new PopupWindow(this);
        myDealsPopup = new PopupWindow(this);
        filterPopup = new PopupWindow(this);
        mainLayout = new LinearLayout(this);

        dealMqqt = new ConnectionMqtt(this);
        dealMqqt.open();

        //also changed the version of google play services on gradle.app from 9.6.1 to
        //7.5.0 cause of compatibility.
        MapsInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("MapsActivity", "Map Ready Bubca");

        //fetchDeals();

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
                        if (marker.getTitle().equals("user")){
                        }else{
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
                        }
                        return doNotMoveCameraToCenterMarker;
                    }
                });

        //Initializing the Options List button and setting an onClick listener to it.
        final ImageButton hamburgerButton = (ImageButton) findViewById(R.id.optionslistbutton);
        hamburgerButton.setOnClickListener(new View.OnClickListener()
        {
            //Function which handles the user pressing the Options List button. If the button is clicked already the popup will be dismissed instead of appearing again.
            //Populating the content view with options_list_popup and shows it on top of the main layout in the centre.
            //Dismisses all other popups when called. While open it handles the options lists buttons by switch case which calls the appropriate function when pressed.
            //Boolean isClickedPop is used to ensure that the Options List popup is dismissed if the Options List button is pressed while the popup is open and vice versa.
            public void onClick(View v) {
                if (isClickedPop == true) {
                    isClickedPop = false;

                    View optPop = getLayoutInflater().inflate(R.layout.options_list_popup, null);
                    optionsPopup.setContentView(optPop);
                    optionsPopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                    optionsPopup.update(700, 620);
                    profilePopup.dismiss();
                    myDealsPopup.dismiss();
                    filterPopup.dismiss();

                    switch (v.getId()) {
                        case R.id.profileButton:
                            profileButtonPressed(v);
                        case R.id.dealsButton:
                            mydealsButtonPressed(v);
                        case R.id.filterButton:
                            filterButtonPressed(v);
                        case R.id.profileBackButton:
                            profileBackButtonPressed(v);
                        case R.id.dealsBackButton:
                            dealsBackButtonPressed(v);
                        case R.id.filterBackButton:
                            filterBackButtonPressed(v);
                            break;
                    }

                }
                else {
                    isClickedPop = true;
                    optionsPopup.dismiss();
                    profilePopup.dismiss();
                    myDealsPopup.dismiss();
                    filterPopup.dismiss();
                }
            }
        });
    }


    private void fetchDeals() {
        fetchHandler = new Handler();
        dealMqqt = new ConnectionMqtt(this);
        dealMqqt.open();
        fetchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String subscribeTopic = "deal/gogodeals/database/deals";
                dealMqqt.subscribe(subscribeTopic,2);

                String payload =   "{ \"id\": \"12345678-1011-M012-N210-112233445566\"," +
                        " \"data\": {" +
                        " \"longitude\": " + mMap.getCameraPosition().target.longitude + "," +
                        " \"latitude\": " +mMap.getCameraPosition().target.latitude + "," +
                        " \"filters\": \"fika\"," +
                        " \"deals\": \"33333333-1011-M012-N210-112233445566\"},}";

                String publishTopic = "deal/gogodeals/deal/fetch";
                dealMqqt.publish(payload,publishTopic);

                Log.i("Sent subscribe",subscribeTopic);
                // fetchDeals();
            }
        }, 5000);
    }

    //Function called by the switch case when back button on My Profile is pressed which dismisses the My Profile popup.
    public void profileBackButtonPressed(View v){
        profilePopup.dismiss();
    }
    //Function called by the switch case when back button on My Deals is pressed which dismisses the My Deals popup.
    public void dealsBackButtonPressed(View v){
        myDealsPopup.dismiss();
    }
    //Function called by the switch case when back button on Deal Filters is pressed which dismisses the Deal Filters popup.
    public void filterBackButtonPressed(View v){
        filterPopup.dismiss();
    }
    // Opens the popup with My Profile on click.
    public void profileButtonPressed(View v){
        View profPop = getLayoutInflater().inflate(R.layout.myprofile, null);
        profilePopup.setContentView(profPop);
        profilePopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        profilePopup.update(700, 620);
    }
    // Opens the popup with My Deals on click.
    public void mydealsButtonPressed(View v){
        View myDealsPop = getLayoutInflater().inflate(R.layout.mydeals, null);
        myDealsPopup.setContentView(myDealsPop);
        myDealsPopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        myDealsPopup.update(700, 620);
    }
    // Opens the popup with Deal Filters on click.
    public void filterButtonPressed(View v){
        View filtersPop = getLayoutInflater().inflate(R.layout.filterslist, null);
        filterPopup.setContentView(filtersPop);
        filterPopup.showAtLocation(mainLayout, Gravity.CENTER, 0 ,0);
        filterPopup.update(700, 620);
    }
   /* public void logoutButtonPressed(View v) {
    }*/

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void  closeButtonClicked(View v){
        popupMessage.dismiss();
    }

    public View getContent(Marker marker) {
        View v = getLayoutInflater().inflate(R.layout.deal_pop_up, null);
        // Getting view from the layout file info_window_layout
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

        //ImageView dealPicture = (ImageView) v.findViewById(R.id.dealPicture);
        // Converting String byte picture to an ImageView
        //String base = components[4].split(",")[1];
        //byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
        //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        //dealPicture.setImageBitmap(decodedByte);
        //Log.d("InfoWindow picture:", components[4]);

        grabbedView = (ImageView) v.findViewById(R.id.grabbedView);
        /*int icon;
        icon = R.drawable.grabbed;
        grabbedView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
        grabbedView.setVisibility(View.INVISIBLE);*/

        // Returning the view containing InfoWindow contents
        return v;
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

            /*//If no deal update has been triggered so far
            if(lastDealUpdatePosition == null) {
                //TODO publish deal read request to server
                lastDealUpdatePosition = new LatLng(location.getLatitude(), location.getLongitude());
            }else if (((lastDealUpdatePosition.longitude < location.getLongitude() + 0.1f) ||
                    (lastDealUpdatePosition.longitude > location.getLongitude() - 0.1f)) &&
                    ((lastDealUpdatePosition.latitude < location.getLatitude() + 0.1f) ||
                            (lastDealUpdatePosition.latitude > location.getLatitude() - 0.1f))
                    ) {
                //TODO publish deal read request to server
                lastDealUpdatePosition = new LatLng(location.getLatitude(), location.getLongitude());
            }*/
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

        mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(myLatLang,myLatLang));
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

    public void animateMarker(final Marker marker, final Location location) {
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
        dealMqqt.subscribe("deal/gogodeals/user/info", 0);
        grabButton = ((Button) v.findViewById(R.id.grabButton));
        grabButton.setVisibility(View.INVISIBLE);
        //grabbedView.setVisibility(View.VISIBLE);
        String payload = "{" +
                "“id”: “33333333-1011-M012-N210-112233445566”," +
                "“data”: {" +
                "“id”: ““12345678-1011-M012-N210-112233445566”," +
                "“deals”: “11111111-1011-M012-N210-112233445566," +
                "22222222-1011-M012-N210-112233445566, 33333333-1011-M012-N210-112233445566”" +
                "},"+
                "}";
        dealMqqt.publish(payload, "deal/gogodeals/deal/save");
        //closePopUpButton = (ImageButton) v.findViewById(R.id.cancelButton);
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Grabbing deal");
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();
    }
}
