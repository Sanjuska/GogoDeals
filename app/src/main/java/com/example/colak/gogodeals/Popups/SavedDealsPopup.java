package com.example.colak.gogodeals.Popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colak.gogodeals.Objects.Deal;
import com.example.colak.gogodeals.MapsActivity;
import com.example.colak.gogodeals.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydeals);
        //dealListView = MapsActivity.dealArrayList;
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

        dealListView = ((ListView) findViewById(R.id.dealList));
        dealAdapter = new ArrayAdapter<Deal>(SavedDealsPopup.this, android.R.layout.simple_list_item_1, MapsActivity.dealArrayList);

        dealListView.setAdapter(dealAdapter);
        dealListView.setClickable(true);
        dealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Extract deal from the clicke list item
                Deal deal = (Deal) parent.getItemAtPosition(position);

                // Create popup window with deal based on the extracted deal
                getContent(deal);

                //remember which deal is being shown, so that it can be removed if ungrabbed
                MapsActivity.grabbedDeal = deal;


            }

        });
    }
            public void getContent(Deal deal) {
                View v = getLayoutInflater().inflate(R.layout.deal_pop_up, null);

                TextView company = (TextView) v.findViewById(R.id.company);
                company.setText(deal.getCompany());

                TextView description = (TextView) v.findViewById(R.id.description);
                description.setText(deal.getDescription());

                TextView price = ((TextView) v.findViewById(R.id.price));
                price.setText(deal.getPrice());

                TextView verificationHeader = ((TextView) v.findViewById(R.id.verificationHeader));
                verificationHeader.setText("Verification code");

                TextView units = ((TextView) v.findViewById(R.id.units));
                units.setText(deal.getVerificationID());

                TextView duration = ((TextView) v.findViewById(R.id.duration));
                duration.setText(deal.getDuration());

                ImageView dealPicture = (ImageView) v.findViewById(R.id.dealPicture);
                dealPicture = deal.getPicture();

                grabbedView = (ImageView) v.findViewById(R.id.grabbedView);
                grabbedView.setVisibility(View.INVISIBLE);
                grabButton = (Button) v.findViewById(R.id.grabButton);
                grabButton.setVisibility(View.INVISIBLE);
                ungrabButton = (Button) v.findViewById(R.id.ungrabButton);
                ungrabButton.setVisibility(View.VISIBLE);

            }
        }