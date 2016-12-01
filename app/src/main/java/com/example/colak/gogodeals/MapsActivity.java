package com.example.colak.gogodeals;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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


    static ConnectionMqtt dealMqtt;

    public static GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;

    Location mLastLocation;

    Handler fetchHandler;

    Marker mPositionMarker;

    Marker lastOpened = null;

    CheckBox food;
    CheckBox clothes;
    CheckBox activities;
    CheckBox stuff;
    CheckBox random;
    Location lastFetched;
    ArrayList<String> filterList;
    boolean fetched = false;


    boolean isClickedPop = true;
    public static ProgressDialog mProgressDlg;

    static PopupWindow popupMessage;
    Button grabButton;
    Button ungrabButton;
    static ImageView grabbedView;
    LocationRequest locationRequest;
    public static String descriptionOfGrabbedDeal;
    public static Deal grabbedDeal;

    PopupWindow filterPopup;
    PopupWindow myDealsPopup;
    ArrayAdapter<Deal> dealAdapter;
    public static List<Deal> dealArrayList;
    static ListView dealListView;

    PopupWindow profilePopup;
    PopupWindow optionsPopup;
    PopupWindow grocodePopup;
    PopupWindow popupDealView;

    LinearLayout mainLayout;

    // Creating an instance of MarkerOptions to set position
    private GoogleApiClient client;
    private ListView grocodeListView;
    private ArrayAdapter<Deal> grocodeAdapter;
    private ArrayList<Deal> grocodeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        filterList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        popupMessage = new PopupWindow(this);
        optionsPopup = new PopupWindow(this);
        profilePopup = new PopupWindow(this);
        popupDealView = new PopupWindow(this);
        myDealsPopup = new PopupWindow(this);
        filterPopup = new PopupWindow(this);
        grocodePopup = new PopupWindow(this);
        mainLayout = new LinearLayout(this);
        filterList = new ArrayList<>();

        //create list adapter for deal list
        dealArrayList = new ArrayList<Deal>();
        dealArrayList.add(new Deal());

        food = (CheckBox) findViewById(R.id.checkBoxFood);
        clothes = (CheckBox) findViewById(R.id.checkBoxClothes);
        activities = (CheckBox) findViewById(R.id.checkBoxActivites);
        stuff = (CheckBox) findViewById(R.id.checkBoxStuff);
        random = (CheckBox) findViewById(R.id.checkBoxRandom);
        




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
                            Bitmap icon;


                            View popup = getContent(marker);
                            popupMessage.setContentView(popup);
                            popupMessage.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                            Display display = getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            int screenWidth = size.x;
                            int screenHeight = size.y;
                            popupMessage.update(screenWidth - 50, screenHeight / 2);

                            popupMessage.setOutsideTouchable(true);
                            popupMessage.setFocusable(true);
                            popupMessage.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            //marker.showInfoWindow();
                            // Re-assign the last openned such that we can close it later
                            lastOpened = marker;
                        }
                        return doNotMoveCameraToCenterMarker;
                    }
                });

        //Initializing the Options List button and setting an onClick listener to it.
        final ImageButton hamburgerButton = (ImageButton) findViewById(R.id.optionslistbutton);
        hamburgerButton.setOnClickListener(new View.OnClickListener() {

            //Function which handles the user pressing the Options List button. If the button is clicked already the popup will be dismissed instead of appearing again.
            //Populating the content view with options_list_popup and shows it on top of the main layout in the centre.
            //Dismisses all other popups when called. While open it handles the options lists buttons by switch case which calls the appropriate function when pressed.
            //Boolean isClickedPop is used to ensure that the Options List popup is dismissed if the Options List button is pressed while the popup is open and vice versa.
            public void onClick(View v) {
                if (isClickedPop == true) {
                    isClickedPop = false;

                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int screenWidth = size.x;
                    int screenHeight = size.y;

                    View optPop = getLayoutInflater().inflate(R.layout.options_list_popup, null);
                    optionsPopup.setContentView(optPop);
                    optionsPopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                    optionsPopup.update(screenWidth - 50, screenHeight / 2);

                    profilePopup.dismiss();
                    myDealsPopup.dismiss();
                    filterPopup.dismiss();

                    switch (v.getId()) {
                        case R.id.profileButton:
                            profileButtonPressed(v);
                            break;

                        case R.id.dealsButton:
                            mydealsButtonPressed(v);
                            break;

                        case R.id.filterButton:
                            filterButtonPressed(v);
                            break;

                        case R.id.profileBackButton:
                            profileBackButtonPressed(v);
                            break;

                        case R.id.dealsBackButton:
                            dealsBackButtonPressed(v);
                            break;

                        case R.id.filterBackButton:
                            filterBackButtonPressed(v);
                            break;

                        case R.id.grocodeButton:
                            grocodeButtonPressed(v);
                    }

                } else {
                    isClickedPop = true;
                    optionsPopup.dismiss();
                    profilePopup.dismiss();
                    myDealsPopup.dismiss();
                    filterPopup.dismiss();

                }
            }
        });
    }

    // Opens the popupwith My Deal on click.
    public void grocodeButtonPressed(View v){
        User user = new User();
        grocodeArrayList = GrocodeHandler.handle(user);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        View grocodeDealsPop = getLayoutInflater().inflate(R.layout.grocode_list, null);
        grocodePopup.setContentView(grocodeDealsPop);
        //myDealsPop.setFocusable(false);
        //myDealsPop.setClickable(false);
        //dealAdapter = new ArrayAdapter<Deal>(MapsActivity.this,R.layout.list_row, dealArrayList);
        grocodeAdapter= new ArrayAdapter<Deal>(MapsActivity.this, android.R.layout.simple_list_item_1,grocodeArrayList);
        // dealListView =((ListView) findViewById(R.id.dealList));
        grocodeListView = ((ListView) myDealsPopup.getContentView().findViewById(R.id.grocodeListView));
        grocodeListView.setAdapter(grocodeAdapter);
        grocodeListView.setClickable(true);
        grocodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Extract deal from the clicke list item
                Deal deal = (Deal)parent.getItemAtPosition(position);

                // Create popup window with deal based on the extracted deal
                View popup = getContent(deal);
                popupDealView.setContentView(popup);
                popupDealView.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int screenWidth = size.x;
                int screenHeight = size.y;
                popupDealView.update(screenWidth - 50, screenHeight / 2);

                popupDealView.setOutsideTouchable(true);
                popupDealView.setFocusable(true);
                popupDealView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //remember which deal is being shown, so that it can be removed if ungrabbed
                grabbedDeal = deal;


            }
        });

        myDealsPopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        myDealsPopup.update(screenWidth - 50, screenHeight / 2);
    }


    public void fetchDeals(String filter) {

        dealMqtt = new ConnectionMqtt(this);

        String subscribeTopic = "deal/gogodeals/database/deals";


                String payload =   "{ \"id\": \"12345678-1011-M012-N210-112233445566\"," +
                        " \"data\": {" +
                        " \"longitude\": " + mLastLocation.getLongitude() + "," +
                        " \"latitude\": " + mLastLocation.getLatitude() + "," +
                        " \"filters\": \""+filter+"\"}}";

                String publishTopic = "deal/gogodeals/deal/fetch";

                dealMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);




    }

    //Function called by the switch case when back button on My Profile is pressed which dismisses the My Profile popup.
    public void profileBackButtonPressed(View v){
        profilePopup.dismiss();
    }
    //Function called by the switch case when back button on My Deal is pressed which dismisses the My Deal popup.
    public void dealsBackButtonPressed(View v){
        myDealsPopup.dismiss();
    }
    //Function called by the switch case when back button on Deal Filters is pressed which dismisses the Deal Filters popup.
    public void filterBackButtonPressed(View v){
        filterPopup.dismiss();
    }

    // Opens the popup with My Profile on click.
    public void profileButtonPressed(View v){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        View profPop = getLayoutInflater().inflate(R.layout.myprofile, null);
        profilePopup.setContentView(profPop);
        profilePopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        profilePopup.update(screenWidth - 50, screenHeight / 2);
    }

    // Opens the popupwith My Deal on click.
    public void mydealsButtonPressed(View v){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

       View myDealsPop = getLayoutInflater().inflate(R.layout.mydeals, null);
        myDealsPopup.setContentView(myDealsPop);
        //myDealsPop.setFocusable(false);
        //myDealsPop.setClickable(false);
        //dealAdapter = new ArrayAdapter<Deal>(MapsActivity.this,R.layout.list_row, dealArrayList);
        dealAdapter= new ArrayAdapter<Deal>(MapsActivity.this, android.R.layout.simple_list_item_1,dealArrayList);
       // dealListView =((ListView) findViewById(R.id.dealList));
        dealListView = ((ListView) myDealsPopup.getContentView().findViewById(R.id.dealList));
        dealListView.setAdapter(dealAdapter);
         dealListView.setClickable(true);
        dealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Extract deal from the clicke list item
                Deal deal = (Deal)parent.getItemAtPosition(position);

                // Create popup window with deal based on the extracted deal
                View popup = getContent(deal);
                popupDealView.setContentView(popup);
                popupDealView.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int screenWidth = size.x;
                int screenHeight = size.y;
                popupDealView.update(screenWidth - 50, screenHeight / 2);

                popupDealView.setOutsideTouchable(true);
                popupDealView.setFocusable(true);
                popupDealView.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //remember which deal is being shown, so that it can be removed if ungrabbed
                grabbedDeal = deal;

            }
        });

        myDealsPopup.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        myDealsPopup.update(screenWidth - 50, screenHeight / 2);
    }

    // Opens the popup with Deal Filters on click.
    public void filterButtonPressed(View v){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        View filtersPop = getLayoutInflater().inflate(R.layout.filterslist, null);
        filterPopup.setContentView(filtersPop);
        filterPopup.showAtLocation(mainLayout, Gravity.CENTER, 0 ,0);
        filterPopup.update(screenWidth - 50, screenHeight / 2);
    }


    public void onCheckboxClicked (View v){

        switch(v.getId()) {
            case R.id.checkBoxFood:
                if(food.isChecked()){
                    filterList.add("food");
                }
                else{
                    filterList.remove(filterList.indexOf("food"));
                }

            case R.id.checkBoxClothes:
                if(clothes.isChecked()){
                    filterList.add("clothes");
                }
                else{
                    filterList.remove(filterList.indexOf("clothes"));
                }

            case R.id.checkBoxActivites:
                if(activities.isChecked()){
                    filterList.add("activities");
                }
                else{
                    filterList.remove(filterList.indexOf("activities"));
                }

            case R.id.checkBoxStuff:
                if(stuff.isChecked()){
                    filterList.add("stuff");
                }
                else{
                    filterList.remove(filterList.indexOf("stuff"));
                }

            case R.id.checkBoxRandom:
                if(random.isChecked()){
                    filterList.add("random");
                }
                else{
                    filterList.remove(filterList.indexOf("random"));
                }
                break;
        }
    }





    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    public View getContent(Marker marker) {
        View v = getLayoutInflater().inflate(R.layout.deal_pop_up, null);


        // Getting view from the layout file info_window_layout
       // String[] components = marker.getSnippet().split(";");
        String[] components = marker.getSnippet().split("€");
        Log.i("json getsnippet ",marker.getSnippet().toString());

        TextView company = (TextView) v.findViewById(R.id.company);
        company.setText(components[0]);

        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(components[1]);

        TextView price = ((TextView) v.findViewById(R.id.price));
        price.setText(components[2]);

        TextView units = ((TextView) v.findViewById(R.id.units));
        units.setText(components[3]);
        Log.d("InfoWindow units:", components[2]);

        TextView duration = ((TextView) v.findViewById(R.id.duration));
        duration.setText(components[4]);
        //Log.d("InfoWindow description:", components[1]);

        ImageView dealPicture = (ImageView) v.findViewById(R.id.dealPicture);
        //Converting String byte picture to an ImageView
        //String base = components[6];
        String base = components[5].split(",")[1];
        byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        dealPicture.setImageBitmap(decodedByte);
        Log.d("InfoWindow picture:", components[5]);


        /*int icon;
        icon = R.drawable.grabbed;
        grabbedView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), icon));
        grabbedView.setVisibility(View.INVISIBLE);*/

        //TextView id = ((TextView) v.findViewById(R.id.idTextView));
       // id.setText(components[6]);
        TextView id = ((TextView) v.findViewById(R.id.idTextView));
        id.setText(components[6]);

        Deal shownDeal = new Deal((String) company.getText(), (String) duration.getText(), (String) price.getText(), dealPicture, (String) description.getText(), (String) id.getText());

        if (dealArrayList.contains(shownDeal)) {
            System.out.println("Deal already grabbed");
            grabbedView = (ImageView) v.findViewById(R.id.grabbedView);
            grabbedView.setVisibility(View.VISIBLE);
            grabButton = (Button) v.findViewById(R.id.grabButton);
            grabButton.setVisibility(View.INVISIBLE);
        } else {
            System.out.println("Deal is not grabbed");
            grabbedView = (ImageView) v.findViewById(R.id.grabbedView);
            grabbedView.setVisibility(View.INVISIBLE);
            grabButton = (Button) v.findViewById(R.id.grabButton);
            grabButton.setVisibility(View.VISIBLE);
        }

        return v;
    }

    public View getContent(Deal deal) {
        View v = getLayoutInflater().inflate(R.layout.deal_pop_up, null);

        TextView company = (TextView) v.findViewById(R.id.company);
        company.setText(deal.getCompany());

        TextView description = (TextView) v.findViewById(R.id.description);
        description.setText(deal.getDescription());

        TextView price = ((TextView) v.findViewById(R.id.price));
        price.setText(deal.getPrice());

        TextView verificationHeader = ((TextView) v.findViewById(R.id.verificationHeader));
        verificationHeader.setText("Verification code");

        TextView units = ((TextView) v.findViewById(R.id.units));
        units.setText(deal.getVerificationID());

        TextView duration = ((TextView) v.findViewById(R.id.duration));
        duration.setText(deal.getDuration());

        ImageView dealPicture = (ImageView) v.findViewById(R.id.dealPicture);
        dealPicture = deal.getPicture();

        grabbedView = (ImageView) v.findViewById(R.id.grabbedView);
        grabbedView.setVisibility(View.INVISIBLE);
        grabButton = (Button) v.findViewById(R.id.grabButton);
        grabButton.setVisibility(View.INVISIBLE);
        ungrabButton = (Button) v.findViewById(R.id.ungrabButton);
        ungrabButton.setVisibility(View.VISIBLE);

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

        filterList.add("food");
        filterList.add("activities");
        filterList.add("random");
        filterList.add("stuff");
        filterList.add("clothes");

        if (!fetched){
            for (String filter :filterList){
                fetchDeals(filter);
            }
            fetched = true;
        }else if(lastFetched != null &&
                mLastLocation != null &&
                lastFetched.getLatitude()+0.2 < mLastLocation.getLatitude() &&
                lastFetched.getLatitude()-0.2 > mLastLocation.getLatitude() &&
                lastFetched.getLongitude()+0.2 < mLastLocation.getLongitude() &&
                lastFetched.getLongitude()-0.2 > mLastLocation.getLongitude()){
            for (String filter :filterList){
                fetchDeals(filter);
            }
            lastFetched = mLastLocation;

        }


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

        dealMqtt = new ConnectionMqtt(this);

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
                " \"user_id\":\"feb00c2b-b4b6-11e6-862e-080027e93e17\"}}";
                //" \"user_id\":\" + user_id + "\"}}";

        String publishTopic = "deal/gogodeals/deal/save";
        dealMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);

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
        mProgressDlg.show();
    }

    public void ungrabButtonPressed(View v) {
        FrameLayout parentLayout = (FrameLayout) ungrabButton.getParent();
        LinearLayout grandParentLayout = (LinearLayout) parentLayout.getParent();
        GridLayout popUpLayout = (GridLayout) grandParentLayout.getParent();

        //extract deal id
        TextView idTV = ((TextView) grandParentLayout.findViewById(R.id.idTextView));
        String deal_id = (String) idTV.getText();

        String payload =   "{ \"id\":\"" + deal_id + "\"," +
                " \"data\": {" +
                " \"user_id\":\"feb00c2b-b4b6-11e6-862e-080027e93e17\"}}";
        //" \"user_id\":\" + user_id + "\"}}";

        String publishTopic = "deal/gogodeals/deal/remove";
        dealMqtt.sendMqtt(payload,publishTopic);

        //extract description of deal, to be stored in grabbed deal list on successful grab
        TextView description = ((TextView) popUpLayout.findViewById(R.id.description));
        MapsActivity.dealArrayList.remove(grabbedDeal);
        popupDealView.dismiss();

        Toast toast = Toast.makeText(getApplicationContext(), "Deal ungrabbed", Toast.LENGTH_SHORT);
        toast.show();

    }
}