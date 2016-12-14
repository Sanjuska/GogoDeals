package com.example.colak.gogodeals.Controllers;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.colak.gogodeals.Objects.IdentifierSingleton;
import com.example.colak.gogodeals.R;
import com.example.colak.gogodeals.Objects.Deal;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Johan Laptop on 2016-11-21.
 */

public class Parsers {

    /*This method takes a topic and a payload message and depending what topic it is
    it calls the correct method corresponding to that topic.
    */

    public void parse(String topic,MqttMessage message) throws JSONException {
        // Checks if this message is related to this instance of the application or to this user
        //if(IdentifierSingleton.SESSION == get_id(message) || IdentifierSingleton.USER == get_id(message)) {
            switch (topic) {
                case "deal/gogodeals/database/deals":
                    try {
                        JSONObject jsonCheck = new JSONObject(new String(message.getPayload()));
                        //Log.i("json checking",jsonCheck.toString());
                        if (!jsonCheck.getString("data").equals("{}")){
                            fetchDealParser(message);
                        }
                    } catch (JSONException e) {
                        Log.e("JsonExeption ",e.toString());
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
                //check users on database
                case "deal/gogodeals/database/users":
                    try {
                        checkEmail(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case "Gro/me@gmail.com/fetch-lists":
                    try {
                        grocodeFetchParser(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                case "deal/gogodeals/database/grocode":
                    try {
                        grocodeListParser(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                case "deal/gogodeals/database/filters":
                    try {
                        setFilters(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //check users on database, login from Facebook
                case "deal/gogodeals/database/facebook":
                    try {
                        checkFacebook(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                //insert new user in databse
                case "deal/gogodeals/database/grabbed":
                    setGrabbedDeals(message);
                    break;

                case "deal/gogodeals/database/update":
                    putFilters();
                    break;
                default:
                    break;
            }
        }

    private void setGrabbedDeals(MqttMessage message) throws JSONException {
        Log.i("grabdeal ","startup parser");
        String jsonString = new String(message.getPayload());
        JSONArray jsonArray;
        JSONObject json1;
        json1  = new JSONObject(jsonString);
        jsonArray = new JSONArray(json1.getJSONArray("data").toString());

        for (int i = 0; i< jsonArray.length();i++) {

            Deal deal = new Deal(jsonArray.getJSONObject(i).getString("client_name"),
                    jsonArray.getJSONObject(i).getString("duration"),
                    jsonArray.getJSONObject(i).getString("price"),
                    jsonArray.getJSONObject(i).getString("picture"),
                    jsonArray.getJSONObject(i).getString("description"),
                    jsonArray.getJSONObject(i).getString("id"));
            Log.i("grabdeals startup ",deal.toString());
            MainActivity.dealArrayList.add(deal);

        }

    }

    private void setFilters(MqttMessage message) throws JSONException {


        JSONObject tmpObj = new JSONObject(new String(message.getPayload()));
        JSONObject tmpObj2 = new JSONObject(tmpObj.get("data").toString());
        String tmpString = new String(tmpObj2.get("filters").toString());
        Log.i("filters get start ",tmpString);
        String [] tmpArray =  tmpString.split(",");

        ArrayList<String> replaceArray = new ArrayList<>();
        for (String tmp : tmpArray) {
            String returnString = tmp.trim();
            replaceArray.add(returnString);
            Log.i("filter added",returnString);
        }


       MainActivity.filterList = replaceArray;

       /* MapsActivity.mMap.clear();
        MapsActivity.mPositionMarker = null;*/
        for (String filter : MainActivity.filterList) {
            MainActivity.messages.fetchDeals(filter,MapsActivity.mLastLocation);
        }

        if (MapsActivity.firstLoad){
            //do nothing
            MapsActivity.firstLoad = false;
        }else{
            OptionsPopup.optionsPopup.startActivity(new Intent(OptionsPopup.optionsPopup, FilterPopup.class));
            OptionsPopup.mProgressDlg.dismiss();
            OptionsPopup.optionsPopup.finish();
        }

        }


    private void grocodeFetchParser(MqttMessage message) throws JSONException {
        String payload = new String(message.getPayload());
        Log.i("Gro", payload);
        if(payload.contains("data")) {
            Log.i("Gro in if", payload);
            JSONArray jsonArray = new JSONArray(new JSONObject(payload).getJSONArray("data").toString());
            MainActivity.messages.getDeals(jsonArray);
        }

    }

    private void grocodeListParser(MqttMessage message) throws JSONException {
        String payload = new String(message.getPayload());
        Log.i("This the payload: ", payload);
        if(payload.contains("data")) {
            JSONArray jsonArray = new JSONArray(new JSONObject(payload).getJSONArray("data").toString());

            ArrayList<Deal> deals = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                deals.add(new Deal(
                        jsonArray.getJSONObject(i).getString("client_name"),
                        jsonArray.getJSONObject(i).getString("duration"),
                        jsonArray.getJSONObject(i).getString("price"),
                        jsonArray.getJSONObject(i).getString("price"),
                        jsonArray.getJSONObject(i).getString("description"),
                        jsonArray.getJSONObject(i).getString("id")));
            }
            MainActivity.groDeals = deals;
            OptionsPopup.optionsPopup.startActivity(new Intent(OptionsPopup.optionsPopup, GroPopup.class));
            OptionsPopup.mProgressDlg.dismiss();
            OptionsPopup.optionsPopup.finish();
            //GroPopup.grocodeArrayList.addAll(deals);
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
        Log.i("json got message ",message.getPayload().toString());
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
        Log.i("grabdeal ","in parser");
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
        DealsPopup.grabbedView.setVisibility(View.VISIBLE);
        // update unit in popup
        DealsPopup.units.setText(String.valueOf(count));
        // add deal to list
        //TextView description = (TextView) MapsActivity.popupMessage.getContentView().findViewById(R.id.description);
        //MapsActivity.dealArrayList.add(MapsActivity.descriptionOfGrabbedDeal);
        MainActivity.grabbedDeal.setVerificationID(verificationID);
        MainActivity.dealArrayList.add(MainActivity.grabbedDeal);
        // add code to deal in list
        DealsPopup.mProgressDlg.dismiss();
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
        IdentifierSingleton.set(id);
        if (emailData.equals(GogouserLogin.email) && passwordData.equals(GogouserLogin.password)){
            GogouserLogin.loginResult=true;
            MainActivity.userID = id;
            Log.i("User", MainActivity.userID);
            GogouserLogin.mProgressDlg.dismiss();
            //gogouserLogin.loginResultReceived();
        }
        else {
            Log.i("7 :", "1");
            Log.i("9 :", "1");
            GogouserLogin.loginResult=false;
            GogouserLogin.mProgressDlg.dismiss();
        }
    }

    public void checkFacebook(MqttMessage message) throws JSONException {
        String id;
        String messageString = new String(message.getPayload());
        JSONObject jsonData;
        jsonData = new JSONObject(messageString);
        jsonData = new JSONObject(jsonData.getString("data"));
        id = jsonData.getString("id");
        Log.i("connection before id ",id);
        IdentifierSingleton.set(id);
        Log.i("connection after id ",id);
        FacebookLogin.mProgressDlg.dismiss();
        //now load next maps activity screen
        Intent gogoApp = new Intent(FacebookLogin.faceBookLogin, MapsActivity.class);
        FacebookLogin.faceBookLogin.startActivity(gogoApp);

        FacebookLogin.faceBookLogin.finish();
    }

    public void putFilters(){

        /*MapsActivity.mMap.clear();
        MapsActivity.mPositionMarker = null;*/
        ArrayList<String> arrayLoop;
        arrayLoop = FilterPopup.filterHandler.filters;
        MainActivity.filterList = arrayLoop;
        for (String filter : arrayLoop) {
            Log.i("filters set",filter);
            MainActivity.messages.fetchDeals(filter,MapsActivity.mLastLocation);
        }
        FilterPopup.filterPopup.startActivity(new Intent(FilterPopup.filterPopup,OptionsPopup.class));
        FilterPopup.mProgressDlg.dismiss();
        FilterPopup.filterPopup.finish();
        Log.i("filter ","finish");




    }
}

