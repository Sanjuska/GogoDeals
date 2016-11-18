package com.example.colak.gogodeals.MqttModule;

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

/**
 * Created by Nikos on 12/11/2016.
 */
public class UserSignup extends AppCompatActivity implements MqttCallback {

    EditText email;
    EditText password;
    EditText confirmpassword;

    Button signup;

    private String newUserEmail;
    private String newUserPassword;
    private String newUserConfirmPassword;

    //mqtt
    private static MqttAndroidClient client;
    private static final String TAG = "ConnectionMqtt";

    //mqtt connectivity
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
            Log.d(TAG, "EPIC FAIL");
        }
    }

    public void saveUserDetails() {
        String topic = "deal/gogodeals/client/new";
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
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuser_signup);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        signup = (Button) findViewById(R.id.signup);
        //signup.setOnClickListener(this);


    }


    public void onClick1(View v) {

        newUserEmail = email.getText().toString();
        newUserPassword = password.getText().toString();
        newUserConfirmPassword = confirmpassword.getText().toString();

        switch (v.getId()) {
            case R.id.signup:
                if (newUserPassword.equals(newUserConfirmPassword)) {
                    //test for accessing UserLogin data (name-email)
                    Toast.makeText(getApplicationContext(), "Email: " + UserLogin.UserEmail, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Password: " + UserLogin.UserName, Toast.LENGTH_SHORT).show();

                }

                else {
                    Toast.makeText(getApplicationContext(), "Confirm Password: " + newUserConfirmPassword, Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}