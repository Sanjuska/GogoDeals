package com.example.colak.gogodeals.Controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.colak.gogodeals.R;

/**
 * @author Sanja Colak
 */

/**
 * This class loads the splash screen and switches over to Main Activity after 2 seconds.
 */
    public class Intro extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    private final int SPLASH_DISPLAY_DURATION = 2000;
        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setContentView(R.layout.intro);

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                // Start new activity after 2 seconds
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(Intro.this,MainActivity.class);
                        Intro.this.startActivity(mainIntent);
                        Intro.this.finish();
                    }
                }, SPLASH_DISPLAY_DURATION);
            }
        }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    Intent mainIntent = new Intent(Intro.this,MainActivity.class);
                    Intro.this.startActivity(mainIntent);
                    Intro.this.finish();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
