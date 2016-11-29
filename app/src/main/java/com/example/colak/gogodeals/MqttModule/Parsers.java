package com.example.colak.gogodeals.MqttModule;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.colak.gogodeals.R;
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
            //Case for fetching deals
            case "deal/gogodeals/database/deals":
                try {
                    fetchDealParser(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //Case for grabbing deals
            case "deal/gogodeals/database/info":
                try {
                    grabbedDealParser(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;


            default:
                break;
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
                    .snippet(companyName + ";" + description + ";" + price + ";" + count + ";" + duration + ";" + picture + ";" + id));
            Log.i("deal added" ,marker.toString());

        }
        MapsActivity.dealMqtt.close();
    }


    private void grabbedDealParser(MqttMessage message) throws JSONException {
       Log.i("poruka", String.valueOf(message.getPayload()));
        // message template according to RFC
        /*{
            “id”: “33333333-1011-M012-N210-112233445566”,
            “data”: {
            “count”: 99,
            “id”: “24818880316702720”
        },
        }*/
        //Log.i("MESSAGE to parse", new String(message.getPayload()));
        String dealID;
        int count = 0;
        String verificationID = null;

        // Split upp payload messageString into components
        String jsonString = new String(message.getPayload());
        JSONObject jsonData;
        jsonData  = new JSONObject(jsonString);
        dealID = jsonData.getString("id");
        jsonData = new JSONObject(jsonData.getString("data"));
        count = jsonData.getInt("count");
        verificationID = jsonData.getString("id");

        MapsActivity.grabbedView.setVisibility(View.VISIBLE);

        // update unit in popup
        TextView units = ((TextView) MapsActivity.popupMessage.getContentView().findViewById(R.id.units));
        units.setText(String.valueOf(count));

        // add deal to list
        //TextView description = (TextView) MapsActivity.popupMessage.getContentView().findViewById(R.id.description);

        //MapsActivity.dealArrayList.add(MapsActivity.descriptionOfGrabbedDeal);
        MapsActivity.dealArrayList.add(MapsActivity.grabbedDeal);

        // add code to deal in list
        MapsActivity.mProgressDlg.dismiss();

        //close communication
        MapsActivity.dealMqtt.close();
    }
}

