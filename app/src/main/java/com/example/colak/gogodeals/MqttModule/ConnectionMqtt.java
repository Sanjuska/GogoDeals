
package com.example.colak.gogodeals.MqttModule;

import android.app.Activity;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


/**
 * Created by colak on 06/10/16.
 */

public class ConnectionMqtt extends Activity implements MqttCallback {
    // Variables used in the class
    private static final String TAG = "ConnectionMqtt";


    static MqttAndroidClient client;
    Activity parent;
    Parsers parsers;
    String payload;
    String sendTopic;
    String receiveTopic;
    int qot;
public ConnectionMqtt(Activity activity){
    this.parent = activity;
    parsers = new Parsers();
    payload = "";
    sendTopic= "";
    receiveTopic ="";
}



    public void sendMqtt(String topic, String payload){
        open();
        this.sendTopic = topic;
        this.payload = payload;
        this.receiveTopic ="";
        this.qot = 0;
    }

    public void sendMqtt(String payload, String sendTopic, String receiveTopic, int qot){
        open();
        this.payload = payload;
        this.sendTopic = sendTopic;
        this.receiveTopic = receiveTopic;
        this.qot = qot;
    }


    //create and establish an MQTT-ConnectionMqtt
    public void open() {
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
                    if (receiveTopic.equals("")){
                        publish(payload,sendTopic);
                        close();
                    }else{
                        subscribe(receiveTopic,qot);
                    }



                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. ConnectionMqtt timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
    // Publishing messages in the -topic- by clicking button
    public void publish(String payload, String topic){
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
    // Subscribing on a topic and getting messages from the publisher
    public void subscribe(final String topic, int qos){
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.i("json subscribed to ",topic);
                    publish(payload,sendTopic);


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
        Log.d(TAG, "connection lost");
    }

    public void close(){
        try {
            client.disconnect();
            Log.i("disconnected","");
        } catch (MqttException e) {
            Log.i("disconnect failed",e.toString());
            e.printStackTrace();
        }
    }

    // When message from publisher arrived, show the deal on the map.
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        parsers.parse(topic,message);

    }

    //Called when publish has been completed and accepted by broker.
    public void deliveryComplete(IMqttDeliveryToken token){

    }


}





