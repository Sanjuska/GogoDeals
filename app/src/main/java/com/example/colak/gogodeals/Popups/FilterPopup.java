package com.example.colak.gogodeals.Popups;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colak.gogodeals.FilterHandler;
import com.example.colak.gogodeals.MapsActivity;
import com.example.colak.gogodeals.Messages;
import com.example.colak.gogodeals.R;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class FilterPopup extends Activity {

    public static FilterHandler filterHandler;
    Button backButton;
    Messages messages;
    public static ProgressDialog mProgressDlg;
    public static Activity filterPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filterslist);
        filterPopup = this;
        messages = new Messages();
        filterHandler = new FilterHandler(this);
        filterHandler.set(MapsActivity.filterList);
        backButton = (Button) findViewById(R.id.filterBackButton);
        postCreate();

    }

    private void postCreate(){

        if (filterHandler.get().contains("food")){
            filterHandler.food.toggle();
        }
        if (filterHandler.get().contains("clothes")){
            filterHandler.food.toggle();
        }
        if (filterHandler.get().contains("stuff")){
            filterHandler.food.toggle();
        }
        if (filterHandler.get().contains("random")){
            filterHandler.food.toggle();
        }
        if (filterHandler.get().contains("alcohol")){
            filterHandler.food.toggle();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.SetFilters(FilterPopup.this,filterHandler.get().toString());
                MapsActivity.filterList = filterHandler.filters;
                mProgressDlg = new ProgressDialog(FilterPopup.this);
                mProgressDlg.setMessage("Setting filters");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
            }
        });
    }

}
