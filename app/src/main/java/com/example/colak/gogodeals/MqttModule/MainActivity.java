package com.example.colak.gogodeals.MqttModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.colak.gogodeals.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "";
    private TextView info;

    private LoginButton loginButton;

    private Button mainLogin;
    private Button mainsignup;
    private Button gogoProfile;

    private TextView welcometext;
    private AccessToken accessToken;
    private AccessTokenTracker accessTokenTracker;

    ConnectionMqtt connection1;
    private Button gogoLoginbtn;
    private EditText gogoUserfield;
    private EditText gogoPassfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        welcometext = (TextView) findViewById(R.id.welcometext);
        mainLogin = (Button) findViewById(R.id.mainLogin);
        mainsignup = (Button) findViewById(R.id.mainsignup);
        gogoProfile = (Button) findViewById(R.id.gogoProfile);



        mainLogin.setOnClickListener(this);
        mainsignup.setOnClickListener(this);
        gogoProfile.setOnClickListener(this);


    }


    @Override

    //Temporary main screen where user can sign in, sign up
    public void onClick(View v) {
        switch (v.getId()) {
            //login for facebook users

            case R.id.mainLogin:
                connection1 = new ConnectionMqtt(this);
                Intent gogoApp = new Intent(MainActivity.this, UserLogin.class);
                startActivity(gogoApp);
                /*String topic = "deal/gogodeals/user/new";
                String payload = "{\"id\":\"1\",\"data\":{\"username\":\""
                        + UserLogin.fbname + "\",\"password\": \"" + Math.random() + "\",\"email\": \"" + UserLogin.fbemail + "\"},}";
                connection1.sendMqtt1(payload, topic);*/

                break;

            //registration for non facebook users
            case R.id.mainsignup:
                Intent gogoApp2 = new Intent(MainActivity.this, newUserSignup.class);
                startActivity(gogoApp2);
                break;

            case R.id.gogoProfile:

                setContentView(R.layout.gogo_profile_login);
                break;




        }
    }
}

