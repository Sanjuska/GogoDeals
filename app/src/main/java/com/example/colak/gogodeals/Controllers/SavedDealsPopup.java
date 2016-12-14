package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by Johan Laptop on 2016-12-05.
 */

public class SavedDealsPopup extends Activity {

    ArrayAdapter<Deal> dealAdapter;
    ListView dealListView;
    ImageView grabbedView;
    Button grabButton;
    Button ungrabButton;
    Button dealsBackButton;
    TextView idTV;
    TextView description;
    TextView company;
    TextView duration;
    TextView price;
    ImageView picture;
    TextView units;
    ImageView dealPicture;
    TextView verificationHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydeals);
        dealListView = (ListView) findViewById(R.id.dealList);
        idTV = (TextView) findViewById(R.id.idTextView);
        description = (TextView) findViewById(R.id.description);
        company = (TextView) findViewById(R.id.company);
        duration = (TextView) findViewById(R.id.duration);
        price = (TextView) findViewById(R.id.price);
        picture = (ImageView) findViewById(R.id.dealPicture);
        company = (TextView) findViewById(R.id.company);
        description = (TextView) findViewById(R.id.description);
        price = ((TextView) findViewById(R.id.price));
        grabbedView = (ImageView) findViewById(R.id.grabbedView);
        units = ((TextView) findViewById(R.id.units));
        duration = ((TextView) findViewById(R.id.duration));
        dealPicture = (ImageView) findViewById(R.id.dealPicture);
        verificationHeader = ((TextView) findViewById(R.id.verificationHeader));
        MapsActivity.grabbedDeal = new Deal();
        postCreate();
    }

    private void postCreate() {

        dealsBackButton = (Button) findViewById(R.id.dealsBackButton);
        dealsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavedDealsPopup.this,OptionsPopup.class));
                finish();
            }
        });

        List<Deal> arrayList = MapsActivity.dealArrayList;
        Log.i("grab ",arrayList.toString());
        dealAdapter = new ArrayAdapter<Deal>(SavedDealsPopup.this, android.R.layout.simple_list_item_1, arrayList);
        dealListView.setAdapter(dealAdapter);
        dealListView.setClickable(true);
        dealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Extract deal from the clicke list item
                Deal deal = (Deal) parent.getItemAtPosition(position);
                Log.i("grab ",deal.toString());
                // Create popup window with deal based on the extracted deal
                getContent(deal);

                //remember which deal is being shown, so that it can be removed if ungrabbed
                MapsActivity.grabbedDeal = deal;


            }

        });
    }
            public void getContent(Deal deal) {
                company.setText(deal.getCompany());
                description.setText(deal.getDescription());
                price.setText(deal.getPrice());
                verificationHeader.setText("Verification code");
                units.setText(deal.getVerificationID());
                duration.setText(deal.getDuration());
                dealPicture = deal.getPicture();
                grabbedView.setVisibility(View.INVISIBLE);
                grabButton.setVisibility(View.INVISIBLE);
                ungrabButton.setVisibility(View.VISIBLE);
            }
        }