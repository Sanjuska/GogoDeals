package com.example.colak.gogodeals.Popups;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colak.gogodeals.MapsActivity;
import com.example.colak.gogodeals.Messages;
import com.example.colak.gogodeals.R;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class OptionsPopup extends Activity {

    Button profileButton, dealsButton, filterButton;
    public static Activity optionsPopup;
    public static ProgressDialog mProgressDlg;
    Messages messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_list_popup);
        optionsPopup = this;
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
                MapsActivity.messages.getFilters(OptionsPopup.this);
                mProgressDlg = new ProgressDialog(OptionsPopup.this);
                mProgressDlg.setMessage("Checking filters");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
            }
        });
    }

}
