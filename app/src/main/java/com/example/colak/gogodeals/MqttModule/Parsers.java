package com.example.colak.gogodeals.MqttModule;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johan Laptop on 2016-11-21.
 */

public class Parsers {

    static GogouserLogin gogouserLogin;

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
            case "deal/gogodeals/database/users":
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

    public void checkEmail(MqttMessage message) throws JSONException {
        String emailData;
        String passwordData;
        String id;
        String messageString = new String(message.getPayload());
        Log.i("fetchEmail: ", String.valueOf(message.getPayload()));
        JSONObject jsonData;

        jsonData = new JSONObject(messageString);

        jsonData = new JSONObject(jsonData.getString("data"));

        emailData = jsonData.getString("email");
        passwordData = jsonData.getString("password");
        id = jsonData.getString("id");

        if (emailData.equals(GogouserLogin.email) && passwordData.equals(GogouserLogin.password)){

            GogouserLogin.loginResult=true;

            MainActivity.userID = id;
            Log.i("User", MainActivity.userID);
            GogouserLogin.mProgressDlg.dismiss();

            gogouserLogin.loginResultReceived();

        }
        else {
            Log.i("7 :", "1");
            Log.i("9 :", "1");
            GogouserLogin.loginResult=false;
            GogouserLogin.mProgressDlg.dismiss();


        }



       /* “id”: “12345678-1011-M012-N210-112233445566”,
        “data”: {
            “id”: “0a1e53be-ac55-11e6-a0a1-8c705aaa0186”,
            “name”: “Bob Bobson”,
            “email”: “Bob@Bobson.se”,
            “password”: “Bobson123”,
            “filters”: null,
            “deals”: null
            }
*/
        /*jsonEmail = new JSONObject(messageString);
        jsonPassword = new JSONObject(messageString);
        String email = jsonEmail.getString("email");
        String password = jsonPassword.getString("password");*/
        GogouserLogin.gogoUserMqtt.close();
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

