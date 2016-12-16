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
import java.util.Date;
import java.util.UUID;

/**
 * Created by Johan Laptop on 2016-11-21.
 */

/**
 * This class consists of parsers recieveing messages via mqtt and controls different parts of the system
 * @author Johan Johansson, Sanja Colak
 */
public class Parsers {

    /**
     * This method takes a topic and a payload message and depending what topic it is
     it calls the correct method corresponding to that topic.
     @param topic
     @param message
     */
    public void parse(String topic,MqttMessage message) throws JSONException {
        // Checks if this message is related to this instance of the application or to this user
        Log.i("identify",message.getPayload().toString());
        Log.i("identify ",IdentifierSingleton.SESSION_ID.toString()+" "+IdentifierSingleton.USER_ID+" "+get_id(message));
        if (IdentifierSingleton.SESSION_ID.equals(get_id(message))|| IdentifierSingleton.USER_ID.equals(get_id(message))){
            Log.i("identify ","worked");
            switch (topic) {
                case "deal/gogodeals/database/deals":
                    try {
                        JSONObject jsonCheck = new JSONObject(new String(message.getPayload()));
                        if (!jsonCheck.getString("data").equals("{}")) {
                            fetchDealParser(message);
                        }
                    } catch (JSONException e) {
                        Log.e("JsonExeption ", e.toString());
                        e.printStackTrace();
                    }
                    break;
                // get info from the deal
                case "deal/gogodeals/database/info":
                    try {
                        grabbedDealParser(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                //get user info on the app
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
                //fetch grocode list
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
    }

    /**
     *  This method enables when user logout from the system and login again,
     * all his saved deal will be listed in the list view
     * @param message
     */
    private void setGrabbedDeals(MqttMessage message) throws JSONException {
        Log.i("grabdeal ","startup parser");
        String jsonString = new String(message.getPayload());
        JSONArray jsonArray;
        JSONObject json1;
        json1  = new JSONObject(jsonString);
        jsonArray = new JSONArray(json1.getJSONArray("data").toString());

        ArrayList<Deal> deals = new ArrayList<>();
        for (int i = 0; i< jsonArray.length();i++) {

            Deal deal = new Deal(jsonArray.getJSONObject(i).getString("client_name"),
                    jsonArray.getJSONObject(i).getString("duration"),
                    jsonArray.getJSONObject(i).getString("price"),
                    jsonArray.getJSONObject(i).getString("picture"),
                    jsonArray.getJSONObject(i).getString("description"),
                    jsonArray.getJSONObject(i).getString("id"));
            Log.i("grabdeals startup ",deal.toString());
            deals.add(deal);

        }
        MainActivity.dealArrayList = deals;

    }
    /**
     *  When user set filters what deals will be visible, this choice will
     * be saved and every time user login again, the filters will be setup on the last chosen filters
     * @param message
     */
    private void setFilters(MqttMessage message) throws JSONException {
        JSONObject tmpObj = new JSONObject(new String(message.getPayload()));
        JSONObject tmpObj2 = new JSONObject(tmpObj.get("data").toString());
        String tmpString = new String(tmpObj2.get("filters").toString());
        Log.i("filters get start ",tmpString);
        String [] tmpArray =  tmpString.split(",");

        // Save chosen filters into list
        ArrayList<String> replaceArray = new ArrayList<>();
        for (String tmp : tmpArray) {
            String returnString = tmp.trim();
            replaceArray.add(returnString);
            Log.i("filter added",returnString);
        }
        // Fetch only those deals which user want's to see
        MainActivity.filterList = replaceArray;
        MainActivity.messages.fetchDeals(replaceArray,MapsActivity.mLastLocation);

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

    /**
     This is the parser for fetching deals to the map
     It takes a payload from a mqqtmessage and turns it into a jsonobject
     it then extracts the different information from that object and
     creates a marker with that information.
     It then puts the marker on the map in Mapsactivity.
     @param message
     */
    private void fetchDealParser(MqttMessage message) throws JSONException {

        Log.i("filters ","fetch started");
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
            Log.i("grabdeal company",companyName);

            // Fetch deal on the location
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

    /**
     This is the parser for grabbing deals which are showed on the map
     It takes a payload from a mqttmessage and turns it into a jsonobject
     it then extracts the different information from that object and
     those information will be used for displaying them to the user ( Verification number),
     and it will extract deals_id from the message and save it together with the user_id.
     This will connect specific user with specific deal.
     @param message
     */
    private void grabbedDealParser(MqttMessage message) throws JSONException {
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
        // update units in popup
        DealsPopup.units.setText(String.valueOf(count));

        // add deal to list
        MainActivity.grabbedDeal.setVerificationID(verificationID);
        MainActivity.dealArrayList.add(MainActivity.grabbedDeal);
        // add code to deal in list
        DealsPopup.mProgressDlg.dismiss();
    }
    /**
     * This method checks if credentials user input correspodents to credentails saved in database
     * @param message
     */
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
        // Checks information
        if (emailData.equals(GogouserLogin.email) && passwordData.equals(GogouserLogin.password)){
            GogouserLogin.loginResult=true;
            MainActivity.userID = id;
            Log.i("User", MainActivity.userID);
            GogouserLogin.mProgressDlg.dismiss();
            Intent gogoAppUserLogIn = new Intent(GogouserLogin.alternativeLogIn, MapsActivity.class);
            GogouserLogin.alternativeLogIn.startActivity(gogoAppUserLogIn);
            GogouserLogin.alternativeLogIn.finish();
          //gogouserLogin.loginResultReceived();
        }
        else {
            Log.i("7 :", "1");
            Log.i("9 :", "1");
            GogouserLogin.loginResult=false;
            GogouserLogin.mProgressDlg.dismiss();
        }
    }
    /** This method takes unique user_id from the user who used Facebook for login, and saves this ID in the database as user_id.
     * @param message
     */
    public void checkFacebook(MqttMessage message) throws JSONException {
        Log.i("timestampfacebook",new Date().getTime()+"");
        String id;
        String messageString = new String(message.getPayload());
        JSONObject jsonData;
        jsonData = new JSONObject(messageString);
        jsonData = new JSONObject(jsonData.getString("data"));
        id = jsonData.getString("id");
        Log.i("connection before id ",id);
        IdentifierSingleton.set(id);
        Log.i("connection after id ",IdentifierSingleton.USER_ID.toString());
        FacebookLogin.mProgressDlg.dismiss();
        //now load next maps activity screen
        Intent gogoApp = new Intent(FacebookLogin.faceBookLogin, MapsActivity.class);
        FacebookLogin.faceBookLogin.startActivity(gogoApp);
        FacebookLogin.faceBookLogin.finish();
    }
    /**
     * When filter is chosen, this method put this choice in the database
     */
    public void putFilters(){

        MapsActivity.mPositionMarker = null;
        ArrayList<String> arrayLoop;
        arrayLoop = FilterPopup.filterHandler.filters;
        MainActivity.filterList = arrayLoop;
        Log.i("filters set",arrayLoop.toString());
        MainActivity.messages.fetchDeals(arrayLoop,MapsActivity.mLastLocation);

        FilterPopup.filterPopup.startActivity(new Intent(FilterPopup.filterPopup,OptionsPopup.class));
        FilterPopup.mProgressDlg.dismiss();
        FilterPopup.filterPopup.finish();
        Log.i("filter ","finish");




    }
}

