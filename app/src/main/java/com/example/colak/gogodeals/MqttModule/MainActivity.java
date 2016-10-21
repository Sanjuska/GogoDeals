package com.example.colak.gogodeals.MqttModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colak.gogodeals.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {

    private TextView info;
    private LoginButton loginButton;

    private CallbackManager callbackManager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        //shows the user which data gets accessed when log in through fb app
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile","email"));

        setContentView(R.layout.mainactivity);
        info = (TextView)findViewById(R.id.info);

        loginButton = (LoginButton)findViewById(R.id.login_button);


        //when fb responds to loginresult, next step is executed by invoking one of the methods below
        //keep user logged in to app
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    Intent gogoApp = new Intent(MainActivity.this, connection.class);

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(gogoApp);

                    }

                    @Override
                    public void onCancel() {

                        Toast.makeText(MainActivity.this, "Login canceled", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException e) {
                        info.setText("Login attempt failed.");
                    }


                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);



    }




}