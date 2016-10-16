package com.example.colak.gogodeals.MqttModule;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.colak.gogodeals.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Created by colak on 06/10/16.
 */
public class connection  extends Activity implements MqttCallback {
    // Variables used in the class
    private static final String TAG = "connection";
    EditText etSendMessage;
    TextView tvGetMessage;
    Button btnSend, btnRefresh;
    MqttAndroidClient client;

    @Override

    //Connect class with xml file
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

        etSendMessage = (EditText) findViewById(R.id.idSendMessage);
        tvGetMessage = (TextView) findViewById(R.id.idGetMessage);
        btnRefresh = (Button) findViewById(R.id.idBtnRefresh);
        btnSend = (Button) findViewById(R.id.idButtonSend);

        // Set locale;
        Locale l = getResources().getConfiguration().locale;
        mqttConnection();
    }


    //create and establish an MQTT-connection
    public void mqttConnection() {

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://176.10.136.208:1883",
                clientId);
        client.setCallback(this);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
    // Publishing messages in the -topic- by clicking button
    public void publishButtonClicked(View v){
        String topic = "topic";
        String payload = etSendMessage.getText().toString();
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
    public void subscribeButtonClicked(View v){
        String topic = "topic";
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
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
    // When message from publisher arrived, show it in the text v¢iew.
    public void messageArrived (String topic, MqttMessage message) throws MqttException{
        String text = tvGetMessage.getText().toString();
        text = text + "\n" + new String (message.getPayload());
        tvGetMessage.setText(text);
        tvGetMessage.setMovementMethod(new ScrollingMovementMethod());


    }



}




