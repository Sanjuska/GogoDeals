package com.example.colak.gogodeals.Controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.colak.gogodeals.R;

/**
 * Created by Nikos on 01/12/2016.
 */

public class GogouserLogin extends AppCompatActivity {

    EditText loginEmail;
    EditText loginPassword;
    Button loginBtn;
    public static ProgressDialog mProgressDlg;
    public static boolean loginResult;
    public static String email;
    public static String password;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    /**
     * Method for alternate login with validating progress dialog while
     * user's credentials are validated
     */
    private void gogoLogin() {

        email = loginEmail.getText().toString();
        password = loginPassword.getText().toString();

        //message which requested for user login
        MainActivity.messages.alternativeUserLogin(email,password);

        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Validating");
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();
    }

    public void loginResultReceived(){
        Log.i("8 :", String.valueOf(loginResult));

        //if user typed credentials are given right, user logs in
        if (loginResult){
            Toast.makeText(getApplicationContext(), "Login succesfull", Toast.LENGTH_LONG).show();
            Intent login = new Intent (GogouserLogin.this, MapsActivity.class);
            startActivity(login);
        }
        else {
            Toast.makeText(getApplicationContext(), "Wrong credentials", Toast.LENGTH_LONG).show();
        }
    }


}



