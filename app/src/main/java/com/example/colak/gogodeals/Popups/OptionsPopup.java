package com.example.colak.gogodeals.Popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colak.gogodeals.R;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class OptionsPopup extends Activity {

    Button profileButton, dealsButton, filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_list_popup);
        profileButton = (Button) findViewById(R.id.profileButton);
        dealsButton = (Button) findViewById(R.id.dealsButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        postCreate();
    }

    private void postCreate(){
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        dealsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsPopup.this,SavedDealsPopup.class));
                finish();
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsPopup.this,FilterPopup.class));
                finish();
            }
        });
    }

}
