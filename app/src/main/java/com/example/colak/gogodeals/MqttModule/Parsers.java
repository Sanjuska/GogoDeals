package com.example.colak.gogodeals.MqttModule;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by Johan Laptop on 2016-11-21.
 */

public class Parsers {

    public void parse(String topic,MqttMessage message){
        IdentifierSingleton identifierSingleton = IdentifierSingleton.getInstance();

        // Checks if this message is related to this instance of the application or to this user
        if(IdentifierSingleton.SESSION == get_id(message) || IdentifierSingleton.USER == get_id(message)) {
            switch (topic) {
                case "deal/gogodeals/database/deals":
                    try {
                        fetchDealParser(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case "deal/gogodeals/USER/info":

                    break;

                default:
                    break;
            }
        }
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
            String companyName = jsonObject.getString("client_name");


            LatLng latlng = new LatLng(latitude,longitude);

            //Deal marker on the map including popup
            Marker marker = MapsActivity.mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(name)
                    .snippet(companyName + ";" + description + ";" + price + ";" + count + ";" + duration + ";" + picture));
            Log.i("deal added" ,marker.toString());

        }
        MapsActivity.dealMqtt.close();
    }


    /**
     * Get the id from a MqttMessage
     * @param message
     * @return UUID
     */
    public UUID get_id(MqttMessage message) {
        String jsonString = new String(message.getPayload());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            return UUID.fromString(jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}

