package com.example.colak.gogodeals;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

    /*This method takes a topic and a payload message and depending what topic it is
    it calls the correct method corresponding to that topic.
    */
    public void parse(String topic,MqttMessage message){
        IdentifierSingleton identifierSingleton = IdentifierSingleton.getInstance();
        // Checks if this message is related to this instance of the application or to this user
       // if(IdentifierSingleton.SESSION_ID == get_id(message) || IdentifierSingleton.USER_ID == get_id(message)) {
            switch (topic) {
                case "deal/gogodeals/database/deals":
                    try {
                        fetchDealParser(message);
                        Log.i("json parser ",message.toString());
                    } catch (JSONException e) {
                        Log.i("json parser error!!","more error");
                        e.printStackTrace();
                    }
                    break;

                case "deal/gogodeals/database/info":
                    try {
                        grabbedDealParser(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case "deal/gogodeals/user/info":
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
    //}


    /*
    This is the parser for fetching deals to the map
    It takes a payload from a mqqtmessage and turns it into a jsonobject
    it then extracts the different information from that object and
    creates a marker with that information.
    It then puts the marker on the map in Mapsactivity.
     */
    private void fetchDealParser(MqttMessage message) throws JSONException {

        String jsonString = new String(message.getPayload());


        JSONArray jsonArray;
        JSONObject jsonObject;
        JSONObject json1;
        json1  = new JSONObject(jsonString);
        jsonArray = new JSONArray(json1.getJSONArray("data").toString());



        for (int i = 0; i< jsonArray.length();i++){

            jsonObject = jsonArray.getJSONObject(i);
            Log.i("json obect ", jsonObject.toString());
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            int price = jsonObject.getInt("price");
            String picture = jsonObject.getString("picture");
            String description = jsonObject.getString("description");
            double longitude = jsonObject.getDouble("longitude");
            double latitude = jsonObject.getDouble("latitude");
            String filters = jsonObject.getString("filters");
            String durationTmp1 = jsonObject.getString("duration");
            String durationTmp2 = durationTmp1.replace("T"," ");
            String duration = durationTmp2.replace("Z"," ");
            int count = jsonObject.getInt("count");
            String client_id = jsonObject.getString("client_id");
            String companyName = jsonObject.getString("client_name");


            LatLng latlng = new LatLng(latitude,longitude);


                if(filters.equals("clothes")) {
                    //Deal marker on the map including popup
                    MapsActivity.mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(name)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.clothes))
                            .snippet(companyName + "€" + description + "€" + price + "€" + count + "€" + duration + "€" + picture + "€" + id));

                }else if(filters.equals("food")){
                    //Deal marker on the map including popup
                   MapsActivity.mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(name)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.food))
                           .snippet(companyName + "€" + description + "€" + price + "€" + count + "€" + duration + "€" + picture + "€" + id));

                }else if(filters.equals("alcohol")){
                    //Deal marker on the map including popup
                    MapsActivity.mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(name)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.alcohol))
                            .snippet(companyName + "€" + description + "€" + price + "€" + count + "€" + duration + "€" + picture + "€" + id));
                }else if(filters.equals("random")){
                    //Deal marker on the map including popup
                   MapsActivity.mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(name)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.random))
                           .snippet(companyName + "€" + description + "€" + price + "€" + count + "€" + duration + "€" + picture + "€" + id));
                }else if(filters.equals("stuff")){
                    //Deal marker on the map including popup
                   MapsActivity.mMap.addMarker(new MarkerOptions()
                            .position(latlng)
                            .title(name)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.stuff))
                           .snippet(companyName + "€" + description + "€" + price + "€" + count + "€" + duration + "€" + picture + "€" + id));
                }




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
        MapsActivity.grabbedDeal.setVerificationID(verificationID);
        MapsActivity.dealArrayList.add(MapsActivity.grabbedDeal);

        // add code to deal in list
        MapsActivity.mProgressDlg.dismiss();

        //close communication
        MapsActivity.dealMqtt.close();
    }


}

