package com.example.colak.gogodeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class newUserSignup extends AppCompatActivity implements MqttCallback {
    // Variables used in the class
    private static final String TAG = "ConnectionMqtt";
    EditText regUsername;
    EditText regEmail;
    EditText regPassword;
    Button gogosignup;

    static MqttAndroidClient client;


    @Override

    //Connect class with xml file
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuser_signup);

        regUsername = (EditText) findViewById(R.id.regUsername);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regPassword = (EditText) findViewById(R.id.regPassword);
        gogosignup = (Button) findViewById(R.id.gogosignup);


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


    //Register user without facebook profile
    public void registerGogouser(View V) {

        //sign up text fields
        String regUser = regUsername.getText().toString();
        String regMail = regEmail.getText().toString();
        String regPass = regPassword.getText().toString();

        String topic = "deal/gogodeals/user/new";

        //guards for empty fields, username have to be different than password or email
        if (regUser.isEmpty() || regMail.isEmpty() || regPass.isEmpty()){
            Toast.makeText(getApplicationContext(), "Credential fields cannot be empty", Toast.LENGTH_SHORT).show();}

            if (regUser.equals(regPass)){
                Toast.makeText(getApplicationContext(), "Username cannot be the same as password", Toast.LENGTH_SHORT).show();
            }

        else if ((!regUser.isEmpty() && !regPass.isEmpty() && !regMail.isEmpty())){
            if ((!regUser.equals(regPass) && !regMail.equals(regPass)
                    && !regUser.equals(regMail.toString()))) {
                Toast.makeText(getApplicationContext(), "Welcome:" + regUser, Toast.LENGTH_SHORT).show();


                String payload = "{\"id\":\"1\",\"data\":{\"username\":\""
                        + regUser + "\",\"password\": \"" + regPass + "\",\"email\": \"" + regMail + "\"},}";


                byte[] encodedPayload;
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);

                    //clear fields
                    regUsername.getText().clear();
                    regPassword.getText().clear();
                    regEmail.getText().clear();

                    //finish activity and login instantly to app
                    finish();
                    startActivity(new Intent(this, MapsActivity.class));

                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
    }}

    //Called when publish has been completed and accepted by broker.
    public void deliveryComplete(IMqttDeliveryToken token){

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    // When message from publisher arrived, show it in the text v¢iew.
    public void messageArrived (String topic, MqttMessage message) throws MqttException{

    }

}

