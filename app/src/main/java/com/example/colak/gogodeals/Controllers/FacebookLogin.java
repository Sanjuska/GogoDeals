package com.example.colak.gogodeals.Controllers;

/**
 * Created by Nikos on 12/11/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
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

public class FacebookLogin extends AppCompatActivity {


    private static final String TAG = "Test@ " ;
    private TextView info;
    public static Activity faceBookLogin;
    private LoginButton loginButton;

    public static ProgressDialog mProgressDlg;

    private CallbackManager callbackManager;

    @Override
        protected void onCreate ( final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Facebook initialization
         */
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.mainactivity);
        info = (TextView) findViewById(R.id.info);
        faceBookLogin = this;
        /**
         * Facebook login button
         */
        loginButton = (LoginButton) findViewById(R.id.login_button);

        /**
         * Shows the user which data gets accessed when log in through fb app
         */
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email"));

        /**
         * When fb responds to loginresult, next step is executed by invoking one of the methods below keeping user logged in to app
         */
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {


                         // When fb credentials are correct, user logins to gogodeals
                         Intent gogoApp = new Intent(FacebookLogin.this, MapsActivity.class);
                         startActivity(gogoApp);

                        JSONObject object;

                         // Fetching facebook user data through JSON object: username and email to store it into our db

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        Log.i("LoginActivity Response ", response.toString());

                                                try {
                                                    String name = object.getString("first_name");
                                                    String lastName = object.getString("last_name");
                                                    String email = object.getString("email");
                                                    Log.i("FBdata: ", name + " " + lastName);
                                                   MainActivity.messages.saveFacebook(name,email,lastName,object);

                                                } catch (JSONException e) {
                                                   e.printStackTrace();
                                                }
                                            }
                                });


                         // Bundle which parses the values we need from logged in user
                         // by acquiring them as parameters

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "first_name, last_name, email");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }


                    @Override
                     public void onCancel() {
                         LoginManager.getInstance().logOut();
                         Intent gogoAppMainscreen = new Intent(FacebookLogin.this, MainActivity.class);
                         startActivity(gogoAppMainscreen);

                          Toast.makeText(FacebookLogin.this, "Login canceled", Toast.LENGTH_SHORT).show();
                      }
                     @Override
                     public void onError(FacebookException e) {
                         info.setText("Login attempt failed.");
                         Log.e("Failed: ", e.toString());
                     }
                }
        );
        Log.i("Bubca", "got here");
        loginProgressScreen();

    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    /**
     * Show validation screen while waiting for response from GogoDeals Erlang module
     */
    void loginProgressScreen() {
        mProgressDlg = new ProgressDialog(this);
        mProgressDlg.setMessage("Validating Facebook login");
        mProgressDlg.setCancelable(false);
        mProgressDlg.show();

    }

}

