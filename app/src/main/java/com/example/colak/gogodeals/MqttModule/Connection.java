package com.example.colak.gogodeals.MqttModule;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by colak on 2016-11-01.
 */

public class Connection implements MqttCallback{

    /**
     * Created by colak on 06/10/16.
     */
    // Variables used in the class
    private static final String TAG = "ConnectionMqtt";
    static MqttAndroidClient client;
    Activity parent;


    //Constructor
    public Connection(Activity activity)  {
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
    public void showDealOnTheMap(){
        String topic = "topic";
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }

            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void connectionLost(Throwable cause){
        Log.d(TAG, "Connection lost");
        System.exit(1);
    }
    //Called when publish has been completed and accepted by broker.
    public void deliveryComplete(IMqttDeliveryToken token){

    }
    // When message from publisher arrived, show the deal on the map.
    public void messageArrived (String topic, MqttMessage message) throws MqttException{
        String mess = new String (message.getPayload());
        Log.e("TEST", mess);
        String [] messArray = mess.split("coordinates:");
        mess = messArray[1];
        messArray = mess.split(",");
        String latitude = messArray[0];
        latitude =latitude.substring(1, latitude.length()-3);
        //mess = messArray[1];
        Log.d("here Latitude", latitude);
        messArray = mess.split("description");
        String longitude = messArray[1];
        longitude = longitude.substring(0,longitude.length()-2);
        Log.d(" here Longitude", longitude);
        LatLng positionDeal = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        MapsActivity.mMap.addMarker(new MarkerOptions().position(positionDeal));



    }
}

