package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colak.gogodeals.R;

/**
 * Created by Johan Laptop on 2016-12-05.
 * @author Olle Renard, Johan Johansson
 */

public class OptionsPopup extends Activity {

    Button profileButton, dealsButton, filterButton, groButton;
    public static Activity optionsPopup;
    public static ProgressDialog mProgressDlg;

    /**
     * Sets options_list_popup as layout. Initiates all buttons in options menu.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_list_popup);
        optionsPopup = this;
        profileButton = (Button) findViewById(R.id.profileButton);
        dealsButton = (Button) findViewById(R.id.dealsButton);
        filterButton = (Button) findViewById(R.id.filterButton);
        groButton = (Button) findViewById(R.id.grocodeButton);
        postCreate();
    }

    /**
     * Set onClickListener on profileButton, dealsButton, filterButton and groButton.
     * If one of these buttons are clicked a new activity is created and the specified popup is opened.
     */
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
                filterButton.setClickable(false);
                mProgressDlg = new ProgressDialog(OptionsPopup.this);
                mProgressDlg.setMessage("Getting filter");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                MainActivity.messages.getFilters();
            }
        });
        groButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groButton.setClickable(false);
                mProgressDlg = new ProgressDialog(OptionsPopup.this);
                mProgressDlg.setMessage("Matching with deals");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                MainActivity.messages.getFromGrocode();
            }
        });
    }

}
