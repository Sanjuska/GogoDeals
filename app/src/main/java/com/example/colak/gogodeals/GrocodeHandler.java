package com.example.colak.gogodeals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by mattias on 2016-11-30.
 */

public class GrocodeHandler {

    private static ConnectionMqtt connectionMqtt;
    private ArrayList<String> items;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;


    public static void getFromGrocode(ConnectionMqtt c){
        connectionMqtt = c;

        String subscribeTopic = "Gro/" + IdentifierSingleton.USER.getEmail();


        String payload =
                "{ " +
                        " \"client_id\": \"" + IdentifierSingleton.USER.getEmail() +  "\"," +
                        " \"list\": \"food\"" +
                        " \"request\": \"fetch\"," +
                        " \"data\": \"\" " +
                "}";

        String publishTopic = "Gro/" + IdentifierSingleton.USER.getEmail();

        connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);
    }

    public static void getDeals(JSONArray jsonArray){
        String subscribeTopic = "deal/gogodeals/database/grocode";

        String payload =   "{ \"id\": \"" + IdentifierSingleton.USER_ID + "\"," +
                " \"data\": " + jsonArray.toString() + "}";

        String publishTopic = "deal/gogodeals/deal/grocode";

        connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);

    }
}
