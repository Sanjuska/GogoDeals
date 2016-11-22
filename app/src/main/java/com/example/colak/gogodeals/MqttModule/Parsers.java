package com.example.colak.gogodeals.MqttModule;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.colak.gogodeals.MqttModule.MapsActivity.mMap;

/**
 * Created by Johan Laptop on 2016-11-21.
 */

public class Parsers {

    public void parse(String topic,MqttMessage message){
        switch (topic){
            case "deal/gogodeals/database/deals":
                fetchDealParser(message);
                break;

            case "deal/gogodeals/user/info":
                break;

            default:
                break;
        }
    }


    public void fetchDealParser(MqttMessage message) {

        Log.i("subscribe in parser",message.toString());
        String messageString = new String(message.getPayload());

        String[] messComponents = messageString.split(""); //Split the payload message on pieces ;

        String msg = new String(message.getPayload());
        String type;
        JSONArray json = null;
        JSONObject jsonObj = null;
        String lat = "";
        try {
            json = new JSONArray(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("json array no work",e.toString());
        }

        for (int i = 0; i<= json.length();i++){
            try {
                jsonObj = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("json object no work",e.toString());
            }
            try {
                lat = jsonObj.get("latitude").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i("json",lat+"");

        for (String str : messComponents){
            Log.i("subscribe split1",str);
            String[] dealComponents = messageString.split(",");
            for (String str2 : dealComponents){
            }

            String company = dealComponents[0].split(":")[1];
            Log.i("compay",company);
            String coordinates = dealComponents[1].split(":")[1];
            Log.i("coordinates",coordinates);
            String description = dealComponents[2].split(":")[1];
            Log.i("description",description);
            String price = dealComponents[3].split(":")[1];
            Log.i("price",price);
            String units= dealComponents[4].split(":")[1];
            Log.i("units",units);
            String duration = dealComponents[5].split(":")[1];
            Log.i("duration",duration);
            String dealPicture = dealComponents[6].split(":")[1];
            Log.i("dealPicture",dealPicture);
            String placeholder1 = dealComponents[7].split(":")[1];
            Log.i("placeholder1",placeholder1);
            String placeholder2 = dealComponents[8].split(":")[1];
            Log.i("placeholder2",placeholder2);
            String placeholder3 = dealComponents[9].split(":")[1];
            Log.i("placeholder3",placeholder3);
            String placeholder4 = dealComponents[10].split(":")[1];
            Log.i("placeholder4",placeholder4);
            String placeholder5 = dealComponents[11].split(":")[1];
            Log.i("placeholder5",placeholder5);
            String placeholder6 = dealComponents[12].split(":")[1];
            Log.i("placeholder6",placeholder6);
            String placeholder7 = dealComponents[13].split(":")[1];
            Log.i("placeholder7",placeholder7);


            String tempString = null;
            tempString = coordinates.split(",")[0];
            String latitude = tempString.substring(14, tempString.length());
            tempString = null;
            tempString = coordinates.split(",")[1];
            String longitude = tempString.substring(1, tempString.length() - 1);


            //Finding position of the deal on the map
            LatLng dealPosition = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

            //Deal marker on the map including popup
            mMap.addMarker(new MarkerOptions()
                    .position(dealPosition)
                    .title(company)
                    .snippet(description + ";" + price + ";" + units + ";" + duration + ";" + dealPicture));
            Log.i("deal added" ,messageString);

        }

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
        //LatLng dealPosition = new LatLng(57.70776, 11.938287);
        //Rimfrostgatan
        LatLng dealPosition = new LatLng(57.7306506, 11.891138100000035);

        //Deal marker on the map including popup
        mMap.addMarker(new MarkerOptions()
                .position(dealPosition)
                .title(company)
                .snippet(description + ";" + price + ";" + units + ";" + duration + ";"));

    }
}
