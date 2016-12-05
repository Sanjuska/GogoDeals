package com.example.colak.gogodeals.Popups;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colak.gogodeals.Objects.Deal;
import com.example.colak.gogodeals.MapsActivity;
import com.example.colak.gogodeals.R;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class DealsPopup extends Activity {

    Button grabButton;
    ImageView grabbedView;
    TextView description;
    TextView company;
    TextView price;
    TextView units;
    TextView duration;
    ImageView dealPicture;
    TextView id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deal_pop_up);
        getContent(MapsActivity.currentMarker);
    }

    public void getContent(Marker marker) {

        description = (TextView) findViewById(R.id.description);
        company = (TextView) findViewById(R.id.company);
        price = ((TextView) findViewById(R.id.price));
        units = ((TextView) findViewById(R.id.units));
        duration = ((TextView) findViewById(R.id.duration));
        dealPicture = (ImageView) findViewById(R.id.dealPicture);
        id = ((TextView) findViewById(R.id.idTextView));
        grabbedView = (ImageView) findViewById(R.id.grabbedView);
        grabButton = (Button) findViewById(R.id.grabButton);

        // Getting view from the layout file info_window_layout
        // String[] components = marker.getSnippet().split(";");
        String[] components = marker.getSnippet().split("€");
        Log.i("json getsnippet ", marker.getSnippet().toString());

        company.setText(components[0]);

        description.setText(components[1]);

        price.setText(components[2]);

        units.setText(components[3]);

        duration.setText(components[4]);

        String base = components[5].split(",")[1];
        byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        dealPicture.setImageBitmap(decodedByte);

        id.setText(components[6]);

        Deal shownDeal = new Deal((String) company.getText(), (String) duration.getText(), (String) price.getText(), dealPicture, (String) description.getText(), (String) id.getText());

        if (MapsActivity.dealArrayList.contains(shownDeal)) {

            grabbedView.setVisibility(View.VISIBLE);
            grabButton.setVisibility(View.INVISIBLE);

        } else {

            grabbedView.setVisibility(View.INVISIBLE);
            grabButton.setVisibility(View.VISIBLE);
        }
    }

}
