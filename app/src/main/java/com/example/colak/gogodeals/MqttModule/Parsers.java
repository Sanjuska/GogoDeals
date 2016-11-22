package com.example.colak.gogodeals.MqttModule;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttMessage;

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

        String[] messComponents = messageString.split("data"); //Split the payload message on pieces ;

        for (String str : messComponents){
            Log.i("subscribe split",str);
            String[] dealComponents = messageString.split(",");
            for (String str2 : dealComponents){
                Log.i("subscribe split",str2);
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
}
