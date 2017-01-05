package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

/**
 * Class for the content of an activity with a listview populated with information from the Gro system
 */
public class GroPopup extends Activity {
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

    Button grocodeBackButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocode_list);
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
        grocodeBackButton = (Button) findViewById(R.id.grocodeBackButton);
        grocodeArrayList = MainActivity.groDeals;
    }

    public void postCreate(){
        // When user wants go out from the deal view, it returns him to the Option popup view.
        grocodeBackButton = (Button) findViewById(R.id.dealsBackButton);
        grocodeBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroPopup.this,OptionsPopup.class));
                finish();
            }
        });

        grocodeAdapter= new ArrayAdapter<Deal>(this, android.R.layout.simple_list_item_1,grocodeArrayList);
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

    //When the user click the deal popup opens with specific information
    public void getContent(Deal deal) {
        Log.i("grabdeal ",deal.toString());
        company.setText(deal.getCompany());
        description.setText(deal.getDescription());
        price.setText(deal.getPrice());
        verificationHeader.setText("Verification code");
        units.setText(deal.getVerificationID());
        duration.setText(deal.getDuration());
        if (!deal.getStringPicture().isEmpty()){
            String[] pictureParts = deal.getStringPicture().split(",");
            byte[] decodedString = Base64.decode(pictureParts[1], Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            picture.setImageBitmap(decodedByte);
        }else{
            picture = deal.getPicture();
        }
    }
}
