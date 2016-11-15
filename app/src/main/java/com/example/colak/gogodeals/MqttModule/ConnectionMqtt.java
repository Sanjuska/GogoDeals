
package com.example.colak.gogodeals.MqttModule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ConnectionMqtt extends AppCompatActivity implements MqttCallback {
    // Variables used in the class
    private static final String TAG = "ConnectionMqtt";
    EditText etSendMessage;
    TextView tvGetMessage;
    Button btnSend, btnRefresh;
    static MqttAndroidClient client;

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
    //create and establish an MQTT-ConnectionMqtt
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
                    // Something went wrong e.g. ConnectionMqtt timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
    // Publishing messages in the -topic- by clicking button
    public void publishButtonClicked(View v){
        String topic = "deal/gogodeals/user/new";


        String payload = "{\"id\":\"1\",\"payload_encryption\":\"false\",\"data\":{\"username\":\""
                + UserLogin.UserName+"\",\"password\": \"gener1\",\"email\": \""+UserLogin.UserEmail+"\"},}";

        byte[] encodedPayload;
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    //private void userDetails() throws JSONException{
      //  JSONObject obj = new JSONObject();
        //obj.put("username", etSendMessage.getText().toString());
        //obj.put("password", "testnikos1");
        //obj.put("email", "emailnikos");
        //System.out.println(obj);
       // Log.i("s",obj.toString());
    //}



    // Subscribing on a topic and getting messages from the publisher
    public static void showDealOnTheMap(){
        String topic = "deal/gogodeals/database/users";
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
    public void subscribeButtonClicked(View v){
        String topic = "deal/gogodeals/user/new";

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
        Log.d(TAG, "Deals lost");
        System.exit(1);
    }
    //Called when publish has been completed and accepted by broker.
    public void deliveryComplete(IMqttDeliveryToken token){

    }
    // When message from publisher arrived, show it in the text v¢iew.
    public void messageArrived (String topic, MqttMessage message) throws MqttException{
        //should get a message from broker to know if a username is already on the database
        String text = tvGetMessage.getText().toString();
        text = text + "\n" + new String (message.getPayload());
        tvGetMessage.setText(text);
        Toast.makeText(getApplicationContext(), "Welcome: " + UserLogin.UserName, Toast.LENGTH_SHORT).show();
        tvGetMessage.setMovementMethod(new ScrollingMovementMethod());

        //Log.e("tag",new String (message.getPayload()));
        //String mess = new String (message.getPayload());
        //String [] messArray = mess.split("coordinates:");
        //mess = messArray[1];
        //messArray = mess.split(";");
        //String latitude = messArray[0];
        //messArray = mess.split("description");
        //String longitude = messArray[0];
        //longitude = longitude.substring(0,longitude.length()-2);
        //LatLng positionDeal = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(positionDeal));
        //Log.e("longitude",longitude);
        //Log.e("latitude",latitude);


    }



}





