package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

/*  This class is about deal list where the user can list all deals
* he saved.*/
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
        picture = (ImageView) findViewById(R.id.dealPicture);
        company = (TextView) findViewById(R.id.company);
        description = (TextView) findViewById(R.id.description);
        price = ((TextView) findViewById(R.id.price));
        grabbedView = (ImageView) findViewById(R.id.grabbedView);
        units = ((TextView) findViewById(R.id.units));
        duration = ((TextView) findViewById(R.id.duration));
        dealPicture = (ImageView) findViewById(R.id.dealPicture);
        verificationHeader = ((TextView) findViewById(R.id.verificationHeader));
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
                getContent(deal);

                //remember which deal is being shown, so that it can be removed if ungrabbed
                MainActivity.grabbedDeal = deal;


            }

        });
    }
            //When the user click the deal popup opens with specific information
            public void getContent(Deal deal) {
                company.setText(deal.getCompany());
                description.setText(deal.getDescription());
                price.setText(deal.getPrice());
                verificationHeader.setText("Verification code");
                units.setText(deal.getVerificationID());
                duration.setText(deal.getDuration());
                if (deal.getPicture().equals(null)){
                    byte[] decodedString = Base64.decode(deal.getStringPicture(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    dealPicture.setImageBitmap(decodedByte);
                }else{
                    dealPicture = deal.getPicture();
                }
                //Ungrab button is enabled to be clicked.
                grabbedView.setVisibility(View.INVISIBLE);
                grabButton.setVisibility(View.INVISIBLE);
                ungrabButton.setVisibility(View.VISIBLE);
            }
        }