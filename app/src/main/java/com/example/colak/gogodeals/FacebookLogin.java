package com.example.colak.gogodeals;

/**
 * Created by Nikos on 12/11/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

public class FacebookLogin extends AppCompatActivity {


    private static final String TAG = "Test@ " ;
    private TextView info;

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    //private JSONObject fbObject;

    //public static String fbname, fbemail;

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
                        Intent gogoApp = new Intent(FacebookLogin.this, MapsActivity.class);
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
                                                    //fbObject=object;
                                                    String name = object.getString("first_name");
                                                    String lastName = object.getString("last_name");
                                                    String email = object.getString("email");
                                                    Log.i("FBdata: ", name + " " + lastName);


                                                            String topic = "deal/gogodeals/user/new";
                                                    Log.i("fbData2: ", topic);
                                                            String payload = "{\"id\":\"1\",\"data\":{\"name\":\""
                                                                    + name + lastName + "\",\"email\": \"" + Math.random() + "\",\"password\": \"" + object.getString("email") + "\"}}";
                                                    Log.i("fbData3: ", payload);

                                                    ConnectionMqtt connectionMqtt = new ConnectionMqtt(FacebookLogin.this);
                                                    connectionMqtt.sendMqtt(topic, payload);
                                                            Log.i("while condition: ", name + email);


                                                } catch (JSONException e) {
                                                   e.printStackTrace();
                                                }
                                            }
                                });

                        //bundle which parses the values we need from logged in user
                        //by acquiring them as parameters
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "first_name, last_name, email");
                        request.setParameters(parameters);
                        request.executeAsync();

                        //connection1 = new ConnectionMqtt(FacebookLogin.this);

                        //when user press back, he goes to main screen in order to login again etc.
                        //LoginManager.getInstance().logOut();
                        //finish();
                        //startActivity(gogoAppMainscreen);

                    }
                    /*public void mattiasMethod() {
                        try {
                            fbemail = fbObject.getString("email");
                            Log.i("fbemail: ", fbemail);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }*/

                    @Override
                     public void onCancel() {
                         LoginManager.getInstance().logOut();
                         Intent gogoAppMainscreen = new Intent(FacebookLogin.this, MainActivity.class);
                         startActivity(gogoAppMainscreen);
                         //finish();

                          Toast.makeText(FacebookLogin.this, "Login canceled", Toast.LENGTH_SHORT).show();
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

