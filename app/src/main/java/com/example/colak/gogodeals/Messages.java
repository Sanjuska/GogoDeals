package com.example.colak.gogodeals;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

/**
 * Created by Johan Laptop on 2016-12-05.
 */

public class Messages {

    public Messages(){

    }

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
}
