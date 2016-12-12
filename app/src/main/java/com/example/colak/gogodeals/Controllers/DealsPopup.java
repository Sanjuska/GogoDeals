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

public class DealsPopup extends Activity {


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
        setContentView(R.layout.deal_pop_up);
        ungrabButton = (Button) findViewById(R.id.ungrabButton);
        grabButton = (Button) findViewById(R.id.grabButton);
        postCreate();
        getContent(MapsActivity.currentMarker);
    }

    private void postCreate(){


        grabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabButton.setVisibility(View.INVISIBLE);
                //extract deal id
                MainActivity.messages.saveDeal(id.getText());
                //extract description of deal, to be stored in grabbed deal list on successful grab
                //Deal grabbing
                MapsActivity.grabbedDeal = new Deal((String) company.getText(), (String) duration.getText(), (String) price.getText(), dealPicture, (String) description.getText(), id.getText().toString());
                mProgressDlg = new ProgressDialog(DealsPopup.this);
                mProgressDlg.setMessage("Grabbing deal");
                mProgressDlg.setCancelable(false);
                mProgressDlg.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },3000);
            }
        });

        ungrabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //extract deal id
                String deal_id = (String) id.getText();

                //extract description of deal, to be stored in grabbed deal list on successful grab
                MapsActivity.dealArrayList.remove(MapsActivity.grabbedDeal);
                finish();
                Toast toast = Toast.makeText(getApplicationContext(), "Deal ungrabbed", Toast.LENGTH_SHORT);
                toast.show();
                MainActivity.messages.removeDeal(id.getText());
            }
        });

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


        Log.i("grab ", MapsActivity.dealArrayList.toString());
        Log.i("grab ", shownDeal.toString());

        if (MapsActivity.dealArrayList.contains(shownDeal)) {

            grabbedView.setVisibility(View.VISIBLE);
            grabButton.setVisibility(View.INVISIBLE);

        } else {

            grabbedView.setVisibility(View.INVISIBLE);
            grabButton.setVisibility(View.VISIBLE);
        }
    }

}
