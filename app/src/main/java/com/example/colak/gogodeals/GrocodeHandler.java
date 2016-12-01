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

    private ArrayList<String> items;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    public static ArrayList<Deal> handle(User user) throws JSONException {
        JSONArray grocodeList = getFromGrocode(user);
        JSONArray dealList = getDeals(grocodeList);


        return null;
    }

    private static JSONArray getFromGrocode(User user){

        return null;
    }

    private static JSONArray getDeals(JSONArray jsonArray){

        return null;
    }
}
