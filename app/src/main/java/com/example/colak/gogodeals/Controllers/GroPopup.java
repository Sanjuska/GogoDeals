package com.example.colak.gogodeals.Controllers;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.colak.gogodeals.Controllers.MainActivity;
import com.example.colak.gogodeals.Objects.Deal;
import com.example.colak.gogodeals.R;

import java.util.ArrayList;


public class GroPopup extends AppCompatActivity {
    private ListView grocodeListView;
    public static ArrayAdapter<Deal> grocodeAdapter;
    public static ArrayList<Deal> grocodeArrayList;
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
        setContentView(R.layout.activity_gro_popup);
        grocodeListView = (ListView) findViewById(R.id.grocodeListView);
        idTV = (TextView) findViewById(R.id.idTextView);
        description = (TextView) findViewById(R.id.description);
        company = (TextView) findViewById(R.id.company);
        duration = (TextView) findViewById(R.id.duration);
        price = (TextView) findViewById(R.id.price);
        picture = (ImageView) findViewById(R.id.dealPicture);
        units = ((TextView) findViewById(R.id.units));
        dealPicture = (ImageView) findViewById(R.id.dealPicture);
        verificationHeader = ((TextView) findViewById(R.id.verificationHeader));
        grocodeArrayList = MainActivity.groDeals;
    }

    public void postCreate(){
        grocodeAdapter= new ArrayAdapter<Deal>(this, android.R.layout.simple_list_item_1,grocodeArrayList);
        // dealListView =((ListView) findViewById(R.id.dealList));
        grocodeListView.setAdapter(grocodeAdapter);
        grocodeListView.setClickable(true);
        grocodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Extract deal from the clicke list item
                Deal deal = (Deal)parent.getItemAtPosition(position);

                getContent(deal);

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
    }
}
