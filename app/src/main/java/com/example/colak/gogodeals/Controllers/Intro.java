package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.colak.gogodeals.R;

/**
 * @author Sanja Colak
 */

/**
 * This class loads the splash screen and switches over to Main Activity after 2 seconds.
 */
    public class Intro extends Activity {
        private final int SPLASH_DISPLAY_DURATION = 2000;
        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setContentView(R.layout.intro);


            // Start new activity after 2 seconds

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent mainIntent = new Intent(Intro.this,MainActivity.class);
                    Intro.this.startActivity(mainIntent);
                    Intro.this.finish();
                }
            }, SPLASH_DISPLAY_DURATION);
        }}
