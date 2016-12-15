package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colak.gogodeals.Objects.Deal;
import com.example.colak.gogodeals.R;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

/** This class is gives functionalities to DealPopup view which
 * opens when a deal on the map is clicked, as well as the view of the grabbed deal in a list view
 * of grabbed view.
 */

public class DealsPopup extends Activity {


     // Variables used in the popup view

    Button grabButton;
    public static ImageView grabbedView;
    TextView description;
    TextView company;
    TextView price;
    public static TextView units;
    TextView duration;
    ImageView dealPicture;
    TextView id;
    Button ungrabButton;
    public static ProgressDialog mProgressDlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         // Give a UI to the class
        setContentView(R.layout.deal_pop_up);
         //Two buttons for grabbind deal and deleting the deal
        ungrabButton = (Button) findViewById(R.id.ungrabButton);
        grabButton = (Button) findViewById(R.id.grabButton);
        postCreate();
        getContent(MapsActivity.currentMarker);
    }

    private void postCreate(){


         // When GRAB button is presses (in the dealPopUp view


        grabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // Make button invisible to disable grabbing the deal twice
                grabButton.setVisibility(View.INVISIBLE);
                 // Extract deal id
                MainActivity.messages.saveDeal(id.getText());
                 // Extract information about the deal, to be stored in grabbed deal list on successful grab
                MainActivity.grabbedDeal = new Deal((String) company.getText(), (String) duration.getText(), (String) price.getText(), dealPicture, (String) description.getText(), id.getText().toString());
                 // Dialog which says status of the grabbing opens
                mProgressDlg = new ProgressDialog(DealsPopup.this);
                mProgressDlg.setMessage("Grabbing deal");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                Log.i("grabdeal ","click grab");
                 // When user grab a deal, the popup will close after 3 seconds.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },3000);
            }
        });
         // When ungrab button is pressed
        ungrabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // Extract deal id
                String deal_id = (String) id.getText();
                 // Extract description of deal, to be stored in grabbed deal list on successful grab
                MainActivity.dealArrayList.remove(MainActivity.grabbedDeal);
                finish();
                Toast toast = Toast.makeText(getApplicationContext(), "Deal ungrabbed", Toast.LENGTH_SHORT);
                toast.show();
                 // Remove deal from the user's list, as well as from the database where this deal is connected to the user
                MainActivity.messages.removeDeal(id.getText());
            }
        });

    }

    /**
     * Extract all information about the deal from received message and make them visible in the deal marker
     * @param marker
     */
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

         // Splitting the message into different components
        String[] components = marker.getSnippet().split("€");
        Log.i("json getsnippet ", marker.getSnippet().toString());

        company.setText(components[0]);

        description.setText(components[1]);

        price.setText(components[2]);

        units.setText(components[3]);

        duration.setText(components[4]);

         // Converting the picture
        String base = components[5].split(",")[1];
        byte[] decodedString = Base64.decode(base, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        dealPicture.setImageBitmap(decodedByte);

        id.setText(components[6]);
         // Saving extracted data from the message into Deal which opens when the user click on a marker.
        Deal shownDeal = new Deal((String) company.getText(), (String) duration.getText(), (String) price.getText(), dealPicture, (String) description.getText(), (String) id.getText());

        /*Checking if the deal exists in user's list (what means the user grabbed the deal),
        * if it exists then the user won't be able to grab the same deal again, if
        * it doesn't exist the grab button is visible, what means, the user can grab the deal.*/

        if (MainActivity.dealArrayList.contains(shownDeal)) {

            grabbedView.setVisibility(View.VISIBLE);
            grabButton.setVisibility(View.INVISIBLE);

        } else {

            grabbedView.setVisibility(View.INVISIBLE);
            grabButton.setVisibility(View.VISIBLE);
        }
    }

}
