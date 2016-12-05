package com.example.colak.gogodeals.Popups;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.colak.gogodeals.FilterHandler;
import com.example.colak.gogodeals.R;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class FilterPopup extends Activity {

    public static FilterHandler filterHandler;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filterslist);
        filterHandler = new FilterHandler(this);
        backButton = (Button) findViewById(R.id.filterBackButton);
        postCreate();
    }

    private void postCreate(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterHandler.SetFilters();
                finish();
            }
        });
    }

}
