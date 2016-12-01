package com.example.colak.gogodeals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Nikos on 01/12/2016.
 */

public class GogouserLogin extends AppCompatActivity {

    EditText loginEmail;
    EditText loginPassword;

    Button loginBtn;

    static ConnectionMqtt gogoUserMqtt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gogoUserMqtt = new ConnectionMqtt(this);
        setContentView(R.layout.gogo_profile_login);

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                gogoLogin();
            }
        });
    }

    private void gogoLogin() {

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        String topic = "deal/gogodeals/user/info";
        String payload = "{\"id\":\"12345678-1011-M012-N210-112233445566\",\"data\":{\"email\":\""
                + email + "\",\"password\": \"" + password + "\"},}";

        Log.i("loginfielads: ", email + password);

        gogoUserMqtt.sendMqtt(topic, payload);

    }
}


