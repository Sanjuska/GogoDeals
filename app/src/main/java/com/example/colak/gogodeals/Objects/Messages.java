package com.example.colak.gogodeals.Objects;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.example.colak.gogodeals.Controllers.DealsPopup;
import com.example.colak.gogodeals.Controllers.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class Messages {

    static ConnectionMqtt connectionMqtt = new ConnectionMqtt();


    public void fetchDeals(String filter, Location mLastLocation, Activity activity){
        ConnectionMqtt connectionMqtt = new ConnectionMqtt(activity);
        String subscribeTopic = "deal/gogodeals/database/deals";
        String payload =   "{ \"id\": \"12345678-1011-M012-N210-112233445566\"," +
                " \"data\": {" +
                " \"longitude\": " + mLastLocation.getLongitude() + "," +
                " \"latitude\": " + mLastLocation.getLatitude() + "," +
                " \"filters\": \""+filter+"\"}}";
        String publishTopic = "deal/gogodeals/deal/fetch";
        Log.i("json publish ",payload);
        connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);
    }

    public static void saveDeal(CharSequence idTv, Activity activity){
        ConnectionMqtt connectionMqtt = new ConnectionMqtt(activity);
        String subscribeTopic = "deal/gogodeals/database/info";
        String publishTopic = "deal/gogodeals/deal/save";
        String deal_id = idTv.toString();
        String payload =   "{ \"id\":\"" + deal_id + "\"," +
                " \"data\": {" +
                " \"user_id\":\"" + MainActivity.userID + "\"}}";
        connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);
    }

    public static void removeDeal(CharSequence idTv, DealsPopup activity){
        ConnectionMqtt connectionMqtt = new ConnectionMqtt(activity);
        String deal_id = idTv.toString();
        String publishTopic = "deal/gogodeals/deal/remove";
        String payload =   "{ \"id\":\"" + deal_id + "\"," +
                " \"data\": {" +
                " \"user_id\":\"" + MainActivity.userID + "\"}}";
        connectionMqtt.sendMqtt(payload,publishTopic);
    }

    public static void getFilters(Activity activity) {
        ConnectionMqtt connectionMqtt = new ConnectionMqtt(activity);
        String subscribeTopic = "deal/gogodeals/database/filters";
        String payload =   "{ \"id\": \""+ IdentifierSingleton.USER + "\"," +
                " \"data\": {\"crap\": \"hi\" }}";
        String publishTopic = "deal/gogodeals/user/filter";
        Log.i("filter get",payload);
        connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);
    }

    public static void SetFilters(Activity activity, String strings){
        ConnectionMqtt connectionMqtt = new ConnectionMqtt(activity);

        Log.i("filter ",strings);

        String payload =   "{ \"id\": \"" + IdentifierSingleton.USER + "\"," +
                " \"data\": {" +
             //    "\"id\": \"" + IdentifierSingleton.USER + "\"," +
                " \"filters\": \""+ strings +"\"}}";
        String publishTopic = "deal/gogodeals/user/update";
        String returnTopic = "deal/gogodeals/database/update";
        connectionMqtt.sendMqtt(payload,publishTopic,returnTopic,2);

    }

    public static void saveFacebook(String name, String email, String lastName, JSONObject object){
        String topic = "deal/gogodeals/user/facebook";
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

        connectionMqtt.sendMqtt(payload, topic);
        Log.i("while condition: ", name + email);

        String userSubscribe = "deal/gogodeals/database/facebook";
        connectionMqtt.sendMqtt(payload, topic, userSubscribe, 2);
    }

    public static void saveAlternativeUser(String regUser, String regPass, String regMail){
        //topic and payload which will add user to database
        String topic = "deal/gogodeals/user/new";
        String payload = "{\"id\":\"12345678-1011-M012-N210-112233445566\",\"data\":{\"name\":\""
                + regUser + "\",\"password\": \"" + regPass + "\",\"email\": \"" + regMail + "\"},}";
        connectionMqtt.sendMqtt(payload, topic);
        Log.i("topic payload: ", topic + " " + payload);

    }
}
