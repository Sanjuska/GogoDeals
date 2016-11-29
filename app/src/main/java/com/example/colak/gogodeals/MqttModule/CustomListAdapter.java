/*
package com.example.colak.gogodeals.MqttModule;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.colak.gogodeals.R;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] description;

    public CustomListAdapter(Activity context, String[] description) {
        super(context, R.layout.list_row, description);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.description=description;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);


        txtTitle.setText(description[position]);
        //imageView.setImageResource();
        return rowView;

    };
}*/
