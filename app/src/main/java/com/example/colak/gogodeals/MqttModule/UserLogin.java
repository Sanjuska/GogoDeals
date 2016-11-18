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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class UserLogin extends AppCompatActivity {


    private TextView info;

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    private String Name;
    private String Email;
    public static String UserName;
    public static String UserEmail;
    public static String idfb;
    public int test;




    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //i assume that this method was for retrieving a hash key through log?
        /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.colak.gogodeals",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
        // ---------------------------------

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        test = 2;
        setContentView(R.layout.mainactivity);
        //shows the user which data gets accessed when log in through fb app




        info = (TextView)findViewById(R.
                id.info);

        loginButton = (LoginButton)findViewById(R.id.login_button);

        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email"));
        //when fb responds to loginresult, next step is executed by invoking one of the methods below
        //keep user logged in to app
        //loginButton.registerCallback(callbackManager,
        //new FacebookCallback<LoginResult>() {


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {


                    Intent gogoApp = new Intent(UserLogin.this, MapsActivity.class);


                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        //AccessToken accessToken = loginResult.getAccessToken();
                        //Profile profile = Profile.getCurrentProfile();
                        //Log.d("fbusername : ", fbusername);
                        startActivity(gogoApp);
                        newUserSignup firstLogin = new newUserSignup();
                        //newstuff
                        String topic = "deal/gogodeals/user/new";
                        String payload = "{\"id\":\"1\",\"data\":{\"username\":\""
                                + Name+"\",\"password\": \""+(Math.random()+Math.random())+"\",\"email\": \""+Email+"\"},}";


                        //newstuff

                        //Facebook user data
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.i("LoginActivity Response ", response.toString());

                                        try {
                                            //idfb = object.getString("public_profile");
                                            Name = object.getString("name");
                                            UserName = Name;
                                            //Birthday = object.getString("birthday");
                                            Email = object.getString("email");
                                            UserEmail = Email;
                                            Log.d("Email = ", " " + Email);

                                            //Name1 = Name +"test";
                                            Toast.makeText(getApplicationContext(), "Name: " + Name, Toast.LENGTH_LONG).show();
                                            Toast.makeText(getApplicationContext(), "Email: " + Email, Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(getApplicationContext(), "age: " + Birthday, Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(getApplicationContext(), "" + Name1, Toast.LENGTH_SHORT).show();
                                            //loginity();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                        //if (test==4) {
                        //firstLogin.publish(topic, payload);}
                        LoginManager.getInstance().logOut();
                        finish();


                    }


                    @Override
                    public void onCancel() {
                        LoginManager.getInstance().logOut();
                        Toast.makeText(UserLogin.this, "Login canceled", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException e) {
                        info.setText("Login attempt failed.");
                        Log.e("Failed: ", e.toString());
                    }
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        LoginManager.getInstance().logOut();


    }


}