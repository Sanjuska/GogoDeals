package com.example.colak.gogodeals.MqttModule;

/**
 * Created by Nikos on 12/11/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colak.gogodeals.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class UserLogin extends AppCompatActivity {


    private static final String TAG = "Test@ " ;
    private TextView info;

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    static String Name;
    static String Email;

    ConnectionMqtt connection1;

    @Override
        protected void onCreate ( final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        connection1 = new ConnectionMqtt(this);

        //facebook initialization
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.mainactivity);
        info = (TextView) findViewById(R.id.info);

        //facebook login button
        loginButton = (LoginButton) findViewById(R.id.login_button);

        //shows the user which data gets accessed when log in through fb app
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email"));

        //when fb responds to loginresult, next step is executed by invoking one of the methods below
        //keeping user logged in to app
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        //when fb credentials are correct, user logins to gogodeals
                        Intent gogoApp = new Intent(UserLogin.this, MapsActivity.class);
                        startActivity(gogoApp);
                        JSONObject object;

                         //Fetching facebook user data through JSON object: username and email to store it into our db
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.i("LoginActivity Response ", response.toString());

                                                try {
                                                    connection1 = new ConnectionMqtt(UserLogin.this);
                                                    String name = object.getString("name");
                                                    String email = object.getString("email");
                                                    Log.i("FBdata: ", name + " " + email);

                                                    Toast.makeText(getApplicationContext(), "Name: " + name, Toast.LENGTH_LONG).show();
                                                    Toast.makeText(getApplicationContext(), "Email: " + email, Toast.LENGTH_SHORT).show();



                                                    String topic = "deal/gogodeals/user/new";
                                                    String payload = "{\"id\":\"1\",\"data\":{\"username\":\""
                                                            + object.getString("name") + "\",\"password\": \"" + Math.random()+Math.random() + "\",\"email\": \"" + object.getString("email") + "\"},}";
                                                    connection1.sendMqtt(topic, payload);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                });

                        //bundle which parses the values we need to acquire from logged in user
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "name,email");
                        request.setParameters(parameters);
                        request.executeAsync();


                        //when user press back, he goes to main screen in order to login again etc.
                        LoginManager.getInstance().logOut();
                        finish();
                        //startActivity(gogoAppMainscreen);

                    }

                     @Override
                     public void onCancel() {
                         LoginManager.getInstance().logOut();
                         Intent gogoAppMainscreen = new Intent(UserLogin.this, MainActivity.class);
                         startActivity(gogoAppMainscreen);
                         //finish();

                          Toast.makeText(UserLogin.this, "Login canceled", Toast.LENGTH_SHORT).show();
                      }
                     @Override
                     public void onError(FacebookException e) {
                         info.setText("Login attempt failed.");
                         //LoginManager.getInstance().logOut();
                         //startActivity(gogoAppMainscreen);
                         Log.e("Failed: ", e.toString());
                     }
                }
        );

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    /*public void saveInfo(){
        SharedPreferences preferences = getSharedPreferences("FBcredentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FBname",Name);
        editor.putString("FBemail", Email);
        editor.putString("Authentication_Status","true");
        editor.apply();
    }

    public void seeInfo(){
        SharedPreferences preferences = getSharedPreferences("FBcredentials", Context.MODE_PRIVATE);
        //FBname = preferences.getString("FBname", Name);
        //FBemail = preferences.getString("FBemail", Email);
        Log.i("FB ", Name + Email);
        *//*String topic = "deal/gogodeals/user/new";
        String payload = "{\"id\":\"1\",\"data\":{\"username\":\""
                + Name + "\",\"password\": \"" + Math.random()+Math.random() + "\",\"email\": \"" + Email + "\"},}";
        connection1.sendMqtt1(topic, payload);*//*
    }*/

}

