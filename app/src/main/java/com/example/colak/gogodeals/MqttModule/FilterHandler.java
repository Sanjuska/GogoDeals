package com.example.colak.gogodeals.MqttModule;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.colak.gogodeals.R;

import java.util.ArrayList;

/**
 * Created by mattias on 2016-11-28.
 */

public class FilterHandler extends Activity implements CompoundButton.OnCheckedChangeListener {
    private ArrayList<String> filters;

    private CheckBox food;
    private CheckBox clothes;
    private CheckBox activities;
    private CheckBox stuff;
    private CheckBox random;

    /**
     * Initializes the an empty ArrayList<String> of filters, the checkboxes and connect the
     * checkboxes to a listener
     */
    public FilterHandler(){
        filters = new ArrayList<>();

        food = (CheckBox) findViewById(R.id.checkBoxFood);
        clothes = (CheckBox) findViewById(R.id.checkBoxClothes);
        activities = (CheckBox) findViewById(R.id.checkBoxActivites);
        stuff = (CheckBox) findViewById(R.id.checkBoxStuff);
        random = (CheckBox) findViewById(R.id.checkBoxRandom);

        food.setOnCheckedChangeListener(this);
        clothes.setOnCheckedChangeListener(this);
        activities.setOnCheckedChangeListener(this);
        stuff.setOnCheckedChangeListener(this);
        random.setOnCheckedChangeListener(this);
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
}
