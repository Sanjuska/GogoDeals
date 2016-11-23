package com.example.colak.gogodeals.MqttModule;

import android.util.Log;
import android.view.View;

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

    public void parse(String topic,MqttMessage message){
        switch (topic){
            case "deal/gogodeals/database/deals":
                try {
                    fetchDealParser(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "deal/gogodeals/user/info":

                grabbedDealParser(message);
                break;

            default:
                break;
        }
    }


    public void fetchDealParser(MqttMessage message) throws JSONException {
    public void 

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
    private void grabbedDealParser(MqttMessage message) {
        // message template according to RFC
        /*{
            “id”: “33333333-1011-M012-N210-112233445566”,
            “data”: {
            “count”: 99,
            “id”: “24818880316702720”
        },
        }*/
        String dealID;
        int count;
        String verificationID;

        String messageString = new String(message.getPayload());
        // Split upp messageString into components


        MapsActivity.grabbedView.setVisibility(View.VISIBLE);
        // update unit in popup
        // add deal to list
        // add code to deal in list
        MapsActivity.mProgressDlg.dismiss();


    }

    // A Debug method which places a deal on map. Uncomment the correct position depending on where
    // you want to place the deal
    public void debugDealOnMap() {
        String company = ":TestCompany";
        String description = ":TestDescription";
        String price = ":200";
        String units = ":5";
        String duration = ":1";

        //Finding position of the deal on the map

        //Lindholmen
        LatLng dealPosition = new LatLng(57.70776, 11.938287);
        //Rimfrostgatan
        //LatLng dealPosition = new LatLng(57.7306506, 11.891138100000035);

        //Deal marker on the map including popup
        mMap.addMarker(new MarkerOptions()
                .position(dealPosition)
                .title(company)
                .snippet(description + ";" + price + ";" + units + ";" + duration + ";"));

    }

}

