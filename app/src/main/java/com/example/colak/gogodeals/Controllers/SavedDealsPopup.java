package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colak.gogodeals.Objects.Deal;
import com.example.colak.gogodeals.R;

import java.util.List;

/**
 * This class is about deal list where the user can list all deals
 * he saved.
 */

/**
 * @author Sanja Colak
 */
public class SavedDealsPopup extends Activity {
    ArrayAdapter<Deal> dealAdapter;
    ListView dealListView;
    ImageView grabbedView;
    Button dealsBackButton;



    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydeals);
        dealListView = (ListView) findViewById(R.id.dealList);


        postCreate();
    }
    private void postCreate() {

        // When user wants go out from the deal view, it returns him to the Option popup view.
        dealsBackButton = (Button) findViewById(R.id.dealsBackButton);
        dealsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedDealsPopup.this,OptionsPopup.class));
                finish();
            }
        });
        // In the list save all deals from dealArrayList
        List<Deal> arrayList = MainActivity.dealArrayList;
        //Deal adapter which shows grabbed deal in a simple list view
        dealAdapter = new ArrayAdapter<Deal>(SavedDealsPopup.this, android.R.layout.simple_list_item_1, arrayList);
        dealListView.setAdapter(dealAdapter);
        dealListView.setClickable(true);
        //When deal in the list is clicked
        dealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Extract deal from the clicke list item
                Deal deal = (Deal) parent.getItemAtPosition(position);
                // Create popup window view with deal based on the extracted data
                MainActivity.savedDealShow = deal;
                startActivity(new Intent(SavedDealsPopup.this,DealsPopup.class).putExtra("source","deal"));

                //remember which deal is being shown, so that it can be removed if ungrabbed
                MainActivity.grabbedDeal = deal;
            }

        });
    }

        }