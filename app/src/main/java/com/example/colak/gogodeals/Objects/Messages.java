package com.example.colak.gogodeals.Objects;

import android.app.Activity;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class Messages {
    Activity activity;
    int qos;


    public Messages(Activity activity){
        this.activity = activity;
        qos = 2;

    }


    public void fetchDeals(String filter, Location mLastLocation){
        String subscribeTopic = "deal/gogodeals/database/deals";
        String payload =   "{ \"id\": \"" + IdentifierSingleton.USER +"\"," +
                " \"data\": {" +
                " \"longitude\": " + mLastLocation.getLongitude() + "," +
                " \"latitude\": " + mLastLocation.getLatitude() + "," +
                " \"filters\": \""+filter+"\"}}";
        String publishTopic = "deal/gogodeals/deal/fetch";
        Log.i("json publish ",payload);
        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic,subscribeTopic,qos);
    }
    // JSON message when deal is grabbed
    public void saveDeal(CharSequence idTv){
        String subscribeTopic = "deal/gogodeals/database/info";
        String publishTopic = "deal/gogodeals/deal/save";
        String deal_id = idTv.toString();
        String payload =   "{ \"id\":\"" + deal_id + "\"," +
                " \"data\": {" +
                " \"user_id\":\"" + IdentifierSingleton.USER_ID + "\"}}";
        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic,subscribeTopic,qos);
    }

    // JSON message for getting information about saved deal from the db
    public void getGrabbedDeals(){
        String subscribeTopic = "deal/gogodeals/database/grabbed";
        String publishTopic = "deal/gogodeals/deal/grabbed";
        String payload =   "{ \"id\":\"" + IdentifierSingleton.USER_ID + "\"," +
                " \"data\": \"hi\"}";
        Log.i("grabdeal ",payload);
        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic,subscribeTopic,qos);
    }
    //JSON message when deal is removed from the list
    public void removeDeal(CharSequence idTv){
        String deal_id = idTv.toString();
        String publishTopic = "deal/gogodeals/deal/remove";
        String payload =   "{ \"id\":\"" + deal_id + "\"," +
                " \"data\": {" +
                " \"user_id\":\"" + IdentifierSingleton.USER_ID + "\"}}";
        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic);
    }

    public void getFilters() {
        String subscribeTopic = "deal/gogodeals/database/filters";
        String payload =   "{ \"id\": \""+ IdentifierSingleton.USER_ID + "\"," +
                " \"data\": {\"crap\": \"hi\" }}";
        String publishTopic = "deal/gogodeals/user/filter";
        Log.i("filter get",payload);
        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic,subscribeTopic,qos);
    }

    public void setFilters(String filters){

        Log.i("filter message ",filters);

        String payload =   "{ \"id\": \"" + IdentifierSingleton.USER_ID+ "\"," +
                " \"data\": {" +
                " \"filters\": \""+ filters +"\"}}";
        String publishTopic = "deal/gogodeals/user/update";
        String returnTopic = "deal/gogodeals/database/update";
        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic,returnTopic,qos);

    }

    public void saveFacebook(String name, String email, String lastName, JSONObject object){
        final String topic = "deal/gogodeals/user/facebook";
        Log.i("fbData2: ", topic);
        String payload = null;
        try {
            payload = "{\"id\":\"1\",\"data\":{" +
                    "\"email\": \"" + object.getString("email") + "\"," +
                    "\"name\":\"" + name + " " + lastName + "\"},}";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("fbData3: ", payload);

        //connectionMqtt.sendMqtt(payload, topic);
        Log.i("while condition: ", name + email);

        final String userSubscribe = "deal/gogodeals/database/facebook";
        new ConnectionMqtt(activity).sendMqtt(payload, topic);
        Handler handler = new Handler();
        final String finalPayload = payload;
        handler.postDelayed(new Runnable() {
            public void run() {
                new ConnectionMqtt(activity).sendMqtt(finalPayload, topic, userSubscribe, qos);
            }
        }, 100);


    }

     public void saveAlternativeUser(String regUser, String regPass, String regMail){
        //topic and payload which will add user to database
        String topic = "deal/gogodeals/user/new";
        String payload = "{\"id\":\"12345678-1011-M012-N210-112233445566\",\"data\":{\"name\":\""
                + regUser + "\",\"password\": \"" + regPass + "\",\"email\": \"" + regMail + "\"},}";
         new ConnectionMqtt(activity).sendMqtt(payload, topic);
        Log.i("topic payload: ", topic + " " + payload);

    }

    public  void alternativeUserLogin(String email, String password){

        String topic = "deal/gogodeals/user/info";
        String payload = "{\"id\":\"12345678-1011-M012-N210-112233445566\",\"data\":{\"email\":\""
                + email + "\",\"password\": \"" + password + "\"},}";

        String userSubscribe = "deal/gogodeals/database/users";
        new ConnectionMqtt(activity).sendMqtt(payload, topic, userSubscribe, qos);

        Log.i("loginfielads: ", email + password);
    }

    public void getFromGrocode(){

        String subscribeTopic = "Gro/me@gmail.com/fetch-lists"; //+ IdentifierSingleton.USER.getEmail();

        String payload =
                "{" +
                        " \"client_id\": \"me@gmail.com\"," +
                        " \"request\": \"fetch-lists\"" +
                        "}";

        String publishTopic = "Gro/me@gmail.com/fetch-lists"; // + IdentifierSingleton.USER.getEmail();

        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic,subscribeTopic,qos);
    }

    public void getDeals(JSONArray jsonArray){
        String subscribeTopic = "deal/gogodeals/database/grocode";

        String payload =   "{ \"id\": \"" + IdentifierSingleton.USER_ID + "\"," +
                " \"data\": " + jsonArray.toString() + "}";

        String publishTopic = "deal/gogodeals/deal/grocode";

        new ConnectionMqtt(activity).sendMqtt(payload,publishTopic,subscribeTopic,qos);

    }
}
