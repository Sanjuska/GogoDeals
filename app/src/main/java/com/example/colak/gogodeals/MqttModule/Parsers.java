package com.example.colak.gogodeals.MqttModule;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Johan Laptop on 2016-11-21.
 */

public class Parsers {

    public void parse(String topic,MqttMessage message){
        switch (topic){
            case "deal/gogodeals/database/deals":
                try {
                    fetchDealParser(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            //check users on database
            case "deal/gogodeals/user/info":
                try {
                    checkEmail(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            //insert new user in databse
            case "deal/gogodeals/user/new":
                break;

            default:
                break;
        }
    }

    public static void checkEmail (MqttMessage message) throws JSONException {

        String messageString = new String(message.getPayload());
        JSONObject jsonEmail;
        jsonEmail = new JSONObject(messageString);
        String email = jsonEmail.getString("email");
        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();

    }


    public void fetchDealParser(MqttMessage message) throws JSONException {

        String jsonString = new String(message.getPayload());
        Log.i("json payload parser",jsonString);
        JSONArray jsonArray;
        JSONObject jsonObject;
        JSONObject json1;
        json1  = new JSONObject(jsonString);
        jsonArray = new JSONArray(json1.getJSONArray("data").toString());



        for (int i = 0; i< jsonArray.length();i++){

            jsonObject = jsonArray.getJSONObject(i);
            Log.i("json Object",jsonObject.toString());


            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            int price = jsonObject.getInt("price");
            String picture = jsonObject.getString("picture");
            String description = jsonObject.getString("description");
            double longitude = jsonObject.getDouble("longitude");
            double latitude = jsonObject.getDouble("latitude");
            //String filters = jsonObject.getString("filters");
            String duration = jsonObject.getString("duration");
            int count = jsonObject.getInt("count");
            String client_id = jsonObject.getString("client_id");


            LatLng latlng = new LatLng(latitude,longitude);

            //Deal marker on the map including popup
            Marker marker = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(name)
                    .snippet(description + ";" + price + ";" + count + ";" + duration + ";" + picture));
            Log.i("deal added" ,marker.toString());

        }
        MapsActivity.dealMqtt.close();
    }

}

