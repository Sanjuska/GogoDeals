package com.example.colak.gogodeals.MqttModule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.colak.gogodeals.R;

/**
 * Created by Nikos on 12/11/2016.
 */
public class UserSignup extends Activity implements View.OnClickListener {

    EditText email;
    EditText password;
    EditText confirmpassword;

    Button signup;

    private String newUserEmail;
    private String newUserPassword;
    private String newUserConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        newUserEmail = email.getText().toString();
        newUserPassword = password.getText().toString();
        newUserConfirmPassword = confirmpassword.getText().toString();

        switch (v.getId()) {
            case R.id.signup:
                if (newUserEmail.equals("aa@aa")) {
                    Toast.makeText(getApplicationContext(), "Email: " + newUserEmail, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Password: " + newUserPassword, Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(getApplicationContext(), "Confirm Password: " + newUserConfirmPassword, Toast.LENGTH_SHORT).show();
                }
        }
    }
}