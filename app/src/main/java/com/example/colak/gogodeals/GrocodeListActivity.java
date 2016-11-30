package com.example.colak.gogodeals;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by mattias on 2016-11-30.
 */

public class GrocodeListActivity extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<String> items;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocode_list);

        listView = (ListView) findViewById(R.id.grocodeListView);
        arrayAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        listView.setAdapter(arrayAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
