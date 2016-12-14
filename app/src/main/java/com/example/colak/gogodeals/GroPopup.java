package com.example.colak.gogodeals;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class GroPopup extends AppCompatActivity {
    private ListView grocodeListView;
    public static ArrayAdapter<Deal> grocodeAdapter;
    public static ArrayList<Deal> grocodeArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gro_popup);
        grocodeListView = (ListView) findViewById(R.id.grocodeListView);

        GrocodeHandler.getFromGrocode(new ConnectionMqtt()); //TODO
    }

    public void postCreate(){
        grocodeAdapter= new ArrayAdapter<Deal>(this, android.R.layout.simple_list_item_1,grocodeArrayList);
        // dealListView =((ListView) findViewById(R.id.dealList));
        grocodeListView.setAdapter(grocodeAdapter);
        grocodeListView.setClickable(true);
        grocodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Extract deal from the clicke list item
                Deal deal = (Deal)parent.getItemAtPosition(position);



            }
        });
    }
}
