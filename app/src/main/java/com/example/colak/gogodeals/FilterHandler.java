package com.example.colak.gogodeals;

import android.app.Activity;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by mattias on 2016-11-28.
 */

public class FilterHandler extends MapsActivity implements CompoundButton.OnCheckedChangeListener {
    public ArrayList<String> filters;

    public CompoundButton food;
    public CompoundButton clothes;
    public CompoundButton activities;
    public CompoundButton stuff;
    public CompoundButton random;

    /**
     * Initializes the an empty ArrayList<String> of filters, the checkboxes and connect the
     * checkboxes to a listener
     */
    public FilterHandler(Activity activity){
        filters = new ArrayList<>();

        food = (CompoundButton) activity.findViewById(R.id.checkBoxFood);
        clothes = (CompoundButton) activity.findViewById(R.id.checkBoxClothes);
        activities = (CompoundButton) activity.findViewById(R.id.checkBoxActivites);
        stuff = (CompoundButton) activity.findViewById(R.id.checkBoxStuff);
        random = (CompoundButton) activity.findViewById(R.id.checkBoxRandom);

        food.setOnCheckedChangeListener(this);
        clothes.setOnCheckedChangeListener(this);
        activities.setOnCheckedChangeListener(this);
        stuff.setOnCheckedChangeListener(this);
        random.setOnCheckedChangeListener(this);
        getFilters();
    }

    private void getFilters() {
        ConnectionMqtt connectionMqtt = new ConnectionMqtt(this);
        String subscribeTopic = "deal/gogodeals/databse/filters";
        String payload =   "{ \"id\": \"12345678-1011-M012-N210-112233445566\"," +
                " \"data\": {" + null +"\"}}";
        String publishTopic = "deal/gogodeals/user/filters";
        connectionMqtt.sendMqtt(payload,publishTopic,subscribeTopic,2);
    }

    public void SetFilters(){
        ConnectionMqtt connectionMqtt = new ConnectionMqtt(this);
        String payload =   "{ \"id\": \"12345678-1011-M012-N210-112233445566\"," +
                " \"data\": {" +
                " \"id\": \"12345678-1011-M012-N210-112233445566\"," +
                " \"filters\": \""+ get().toString() +"\"}}";
        String publishTopic = "deal/gogodeals/user/filter";
        connectionMqtt.sendMqtt(payload,publishTopic);

    }

    /**
     * Getter for the ArrayList<String> of filters
     * @return
     */
    public ArrayList<String> get(){
        return filters;
    }

    /**
     * Set the internal ArrayList<String> of filters to a new ArrayList<String> of filters
     * @param filters
     */
    public void set(ArrayList<String> filters){
        this.filters = filters;
    }



    /**
     * Adds an element to the ArrayList<String> of filters
     * @param filter
     */
    private void add(String filter){
        filters.add(filter);
    }

    /**
     * Removes an element from the ArrayList<String> of filters
     * @param filter
     */
    private void remove(String filter){
        if(filters.contains(filter)){
            filters.remove(filter);
        }
    }

    /**
     * Updates the ArrayList<String> of filters if the check boolean is true, otherwise removes the
     * filter String from the ArrayList<String> of filters.
     * @param filter
     * @param check
     */
    private void check(String filter, Boolean check){
        if(check){
            add(filter);
        } else {
            remove(filter);
        }
    }

    /**
     * Checks and updates the buttons if any of them change
     * @param button
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton button, boolean b) {
        switch (button.getId()){
            case R.id.checkBoxFood:
                check("food", b);
                break;
            case R.id.checkBoxClothes:
                check("clothes", b);
                break;
            case R.id.checkBoxActivites:
                check("activities", b);
                break;
            case R.id.checkBoxStuff:
                check("stuff", b);
                break;
            case R.id.checkBoxRandom:
                check("random", b);
                break;
        }
    }

    public ArrayList<String> returnFilterList(){
        return filters;
    }
}