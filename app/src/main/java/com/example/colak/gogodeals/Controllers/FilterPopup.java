package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.colak.gogodeals.Objects.FilterHandler;
import com.example.colak.gogodeals.R;

import java.util.ArrayList;

/**
 * Created by Johan Laptop on 2016-12-05.
 * @author Olle Renard, Johan Johansson, Mattias Landkvist
 */

public class FilterPopup extends Activity {

    Button backButton;
    public static ProgressDialog mProgressDlg;
    public static Activity filterPopup;
    public static FilterHandler filterHandler;
    ArrayList<String> checkFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filterslist);
        filterHandler = new FilterHandler(this);
        filterPopup = this;
        backButton = (Button) findViewById(R.id.filterBackButton);
        checkFilters = MainActivity.filterList;
        filterHandler.filters = MainActivity.filterList;
        Log.i("filters oncreate",filterHandler.filters.toString());
        postCreate();


    }

    /**
     * postCreate checks if the filters ArrayList contains each of the filter strings. For each string which exists in the filter ArrayList
     * the checkbox will be set to true and remove the string from the ArrayList and add one to count, otherwise it will be checked to false.
     * Sets an on click listener on the back button in filterList. Returns the user to the options menu and updates the filters in the database.
     */

    private void postCreate(){

        if (checkFilters.contains("food")){
            filterHandler.food.setChecked(true);
            filterHandler.filters.remove("food");
            filterHandler.count++;
        }else{
            filterHandler.food.setChecked(false);
        }
        if (checkFilters.contains("clothes")){
            filterHandler.clothes.setChecked(true);
            filterHandler.filters.remove("clothes");
            filterHandler.count++;
        }else{
            filterHandler.clothes.setChecked(false);
        }
        if (checkFilters.contains("stuff")){
            filterHandler.stuff.setChecked(true);
            filterHandler.filters.remove("stuff");
            filterHandler.count++;
        }else{
            filterHandler.stuff.setChecked(false);
        }
        if (checkFilters.contains("random")){
            filterHandler.random.setChecked(true);
            filterHandler.filters.remove("random");
            filterHandler.count++;
        }else{
            filterHandler.random.setChecked(false);
        }
        if (checkFilters.contains("activities")){
            filterHandler.activities.setChecked(true);
            filterHandler.filters.remove("activities");
            filterHandler.count++;
        }else{
            filterHandler.activities.setChecked(false);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("filter set to databse",filterHandler.get());
                mProgressDlg = new ProgressDialog(FilterPopup.this);
                mProgressDlg.setMessage("Setting filters");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                MainActivity.messages.setFilters(filterHandler.get());
            }
        });
    }

}
