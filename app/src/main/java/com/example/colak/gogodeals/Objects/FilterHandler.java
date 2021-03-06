package com.example.colak.gogodeals.Objects;

import android.app.Activity;
import android.util.Log;
import android.widget.CompoundButton;

import com.example.colak.gogodeals.Controllers.MapsActivity;
import com.example.colak.gogodeals.R;

import java.util.ArrayList;

/**
 * Created by mattias on 2016-11-28.
 * @author Olle Renard, Johan Johansson, Mattias Landkvist
 */

public class FilterHandler extends MapsActivity implements CompoundButton.OnCheckedChangeListener {
    public ArrayList<String> filters;

    public CompoundButton food;
    public CompoundButton clothes;
    public CompoundButton activities;
    public CompoundButton stuff;
    public CompoundButton random;
    public int count;

    /**
     * Initializes the an empty ArrayList<String> of filters, the checkboxes and connect the
     * checkboxes to a listener
     */
    public FilterHandler(Activity activity){

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
        count = 0;



    }



    /**
     * Getter for the ArrayList<String> of filters
     * @return
     */
    public String get(){
        StringBuilder returnStringBuilder = new StringBuilder();
        for (String filter : filters){
            if (filter.equals(filters.get(filters.size()-1))){
                returnStringBuilder.append(filter);
            }else {
                returnStringBuilder.append(filter + ", ");
            }
        }
        String returnString = returnStringBuilder.toString();

        return returnString;
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
        Log.i("filter check add",filter);
    }

    /**
     * Removes an element from the ArrayList<String> of filters
     * @param filter
     */
    private void remove(String filter){

        if(filters.contains(filter)){
            Log.i("filter check remove",filter);
            filters.remove(filter);
        }else{
            Log.i("filter did not remove",filter);
        }
    }

    /**
     * Updates the ArrayList<String> of filters if the check boolean is true, otherwise removes the
     * filter String from the ArrayList<String> of filters.
     * @param filter
     * @param check
     */
    public void check(String filter, Boolean check){

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