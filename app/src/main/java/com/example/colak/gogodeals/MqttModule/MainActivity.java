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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        mainLogin = (Button) findViewById(R.id.mainLogin);

        mainLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainLogin:
                Intent gogoApp1 = new Intent(MainActivity.this, UserLogin.class);
                startActivity(gogoApp1);
                break;
        }
    }
}