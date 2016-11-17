package com.example.colak.gogodeals.MqttModule;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static com.example.colak.gogodeals.MqttModule.MapsActivity.mMap;

/**
 * Created by colak on 2016-10-27.
 */

public class Deals extends AppCompatActivity implements
        MqttCallback//,
//        GoogleMap.OnMarkerClickListener,
//        GoogleMap.OnInfoWindowClickListener,
//        OnMapReadyCallback
{


    /**
     * Created by colak on 06/10/16.
     */
    // Variables used in the class
    private static final String TAG = "ConnectionMqtt";

    static MqttAndroidClient client;

    public String company;

    Activity parent;


    //Constructor
    public Deals(Activity activity) {
        this.parent = activity;
        mqttConnection();
    }

    //create and establish an MQTT-ConnectionMqtt
    public void mqttConnection() {
        String clientId = MqttClient.generateClientId();

        client = new MqttAndroidClient(parent.getApplicationContext(), "tcp://176.10.136.208:1883",
                clientId);
        client.setCallback(this);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    showDealOnTheMap();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. ConnectionMqtt timeout or firewall problems
                    Log.d(TAG, "onFailure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d(TAG, "EPIC FAIL");
        }
    }

    // Subscribing on a topic and getting messages from the publisher
    public void showDealOnTheMap() {
        String topic = "deal/gogodeals/deal/fetch";
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectionLost(Throwable cause) {
        Log.d(TAG, "Deals lost");
        System.exit(1);
    }

    //Called when publish has been completed and accepted by broker.
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    // When message from publisher arrived, show the deal on the map.
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        String messageString = new String(message.getPayload());
        Log.e("TEST", messageString);
        String[] messComponents = messageString.split(";"); //Split the payload message on pieces ;
        String company = messComponents[0].split("name:")[1];
        Log.d("Bubca Company: ", company);
        String coordinates = messComponents[3];
        Log.d("Bubca Coordinates: ", coordinates);
        String description = messComponents[4];
        Log.d("Bubca description: ", description);
        String price = messComponents[5];
        Log.d("Bubca price: ", price);
        String units = messComponents[6];
        Log.d("Bubca units: ", units);
        String duration = messComponents[7];
        Log.d("Bubca duration: ", duration);
        String dealPicture = messComponents[2];
        Log.d("Bubca picture: ", dealPicture);

        String tempString = null;
        tempString = coordinates.split(",")[0];
        String latitude = tempString.substring(14, tempString.length());
        Log.d("Bubca Latitude: ", latitude);

        tempString = null;
        tempString = coordinates.split(",")[1];
        String longitude = tempString.substring(1, tempString.length() - 1);
        Log.d("Bubca Longitude", longitude);

        //Finding position of the deal on the map
        LatLng dealPosition = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        //Deal marker on the map including popup
        Marker deal = mMap.addMarker(new MarkerOptions()
                .position(dealPosition)
                .title(company)
                .snippet(description + ";" + price + ";" + units + ";" + duration + ";" + dealPicture));
        Log.i("deal added" ,"woop");

    }
}
