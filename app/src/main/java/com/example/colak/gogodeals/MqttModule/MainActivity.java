package com.example.colak.gogodeals.MqttModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.colak.gogodeals.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView info;

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    private Button mainLogin;
    private Button mainsignup;

    private TextView welcometext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        welcometext = (TextView)findViewById(R.id.welcometext);
        mainLogin = (Button) findViewById(R.id.mainLogin);
        mainsignup = (Button)findViewById(R.id.mainsignup);

        mainLogin.setOnClickListener(this);
        mainsignup.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainLogin:
                Intent gogoApp1 = new Intent(MainActivity.this, UserLogin.class);
                startActivity(gogoApp1);
                break;
            case R.id.mainsignup:
                Intent gogoApp2 = new Intent(MainActivity.this, UserSignup.class);
                startActivity(gogoApp2);
                break;
        }
    }
}