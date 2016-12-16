package com.example.colak.gogodeals.Controllers;

import android.app.Activity;
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

/** This class is about Gogodeals user login.
 * @author Nikolaos-Machairiotis Sasopoulos*/

public class GogouserLogin extends AppCompatActivity {

    EditText loginEmail;
    EditText loginPassword;
    Button loginBtn;
    public static ProgressDialog mProgressDlg;
    public static boolean loginResult;
    public static String email;
    public static String password;
    public static Activity alternativeLogIn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gogo_profile_login);
        alternativeLogIn = this;

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                gogoLogin();
            }
        });

        // When fb credentials are correct, user logins to gogodeals
        if (loginResult) {
            Intent gogoAppLogIn = new Intent(GogouserLogin.this, MapsActivity.class);
            startActivity(gogoAppLogIn);
        }
    }

    private void gogoLogin() {

        email = loginEmail.getText().toString();
        password = loginPassword.getText().toString();

        MainActivity.messages.alternativeUserLogin(email,password);

        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Validating");
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();
    }

    public void loginResultReceived(){
        Log.i("8 :", String.valueOf(loginResult));
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



