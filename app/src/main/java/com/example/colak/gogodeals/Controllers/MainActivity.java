package com.example.colak.gogodeals.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.colak.gogodeals.Objects.Deal;
import com.example.colak.gogodeals.Objects.IdentifierSingleton;
import com.example.colak.gogodeals.Objects.Messages;
import com.example.colak.gogodeals.R;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

/** This class opens after Intro. This is the main view with login and registration options.
 * @author Olle Renard, Johan Johansson, Mattias Landkvist, Nikolaos-Machairiotis Sasopoulos, Sanja Colak
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ArrayList<Deal> groDeals;
    private TextView info;

    private LoginButton loginButton;
    public static Messages messages;
    private CallbackManager callbackManager;
    public static ArrayList<String> filterList;
    private Button mainLogin;
    private Button mainsignup;
    private Button gogoProfile;
    public static IdentifierSingleton identifierSingleton;
    public static ArrayList<Deal> dealArrayList;
    public static Deal grabbedDeal;
    public static Deal savedDealShow;
    private TextView welcometext;

    public static String userID;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        messages = new Messages(this);
        identifierSingleton = IdentifierSingleton.getInstance();
        welcometext = (TextView) findViewById(R.id.welcometext);
        mainLogin = (Button) findViewById(R.id.mainLogin);
        mainsignup = (Button) findViewById(R.id.mainsignup);
        gogoProfile = (Button) findViewById(R.id.gogoProfile);


         //  This is the first instantiation of the IdentifierSingleton. This is needed for the
         //   session and user id
        filterList = new ArrayList<String>();

        //  Create deal list
        dealArrayList = new ArrayList<Deal>();

        mainLogin.setOnClickListener(this);
        mainsignup.setOnClickListener(this);
        gogoProfile.setOnClickListener(this);

    }
    @Override

    /**
     * Temporary main screen where user can sign in, sign up
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()) {

             // Login for Facebook users
            case R.id.mainLogin:
                Intent gogoApp = new Intent(MainActivity.this, FacebookLogin.class);
                startActivity(gogoApp);
                break;

             // Registration for non-Facebook users
            case R.id.mainsignup:
                Intent gogoApp2 = new Intent(MainActivity.this, newUserSignup.class);
                startActivity(gogoApp2);
                break;

            case R.id.gogoProfile:
                Intent gogoApp3 = new Intent(MainActivity.this, GogouserLogin.class);
                startActivity(gogoApp3);
                break;
            }
        }
    }
