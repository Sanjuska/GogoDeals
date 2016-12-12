package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colak.gogodeals.Objects.Messages;
import com.example.colak.gogodeals.R;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class FilterPopup extends Activity {

    Button backButton;
    public static ProgressDialog mProgressDlg;
    public static Activity filterPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filterslist);
        filterPopup = this;
        backButton = (Button) findViewById(R.id.filterBackButton);
        postCreate();

    }

    private void postCreate(){

        if (MapsActivity.filterHandler.get().contains("food")){
            MapsActivity.filterHandler.food.toggle();
        }
        if (MapsActivity.filterHandler.get().contains("clothes")){
            MapsActivity.filterHandler.food.toggle();
        }
        if (MapsActivity.filterHandler.get().contains("stuff")){
            MapsActivity.filterHandler.food.toggle();
        }
        if (MapsActivity.filterHandler.get().contains("random")){
            MapsActivity.filterHandler.food.toggle();
        }
        if (MapsActivity.filterHandler.get().contains("alcohol")){
            MapsActivity.filterHandler.food.toggle();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Messages.SetFilters(FilterPopup.this,MapsActivity.filterHandler.get().toString());
                mProgressDlg = new ProgressDialog(FilterPopup.this);
                mProgressDlg.setMessage("Setting filters");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
            }
        });
    }

}
