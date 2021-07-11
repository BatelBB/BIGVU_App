package com.example.bigvu_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomItemList extends ArrayAdapter {
    private ArrayList<String> name;
    private ArrayList<String> description;
    private ArrayList<String> imageid;
    private Activity context;

    public CustomItemList(Activity context, ArrayList<String> name, ArrayList<String> description, ArrayList<String> imageid) {
        super(context, R.layout.row_item, name);
        this.context = context;
        this.name = name;
        this.description = description;
        this.imageid = imageid;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);
        TextView textViewCountry = (TextView) row.findViewById(R.id.textViewName);
        TextView textViewCapital = (TextView) row.findViewById(R.id.textviewDesc);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.person_image);



        textViewCountry.setText(name.get(position));
        textViewCapital.setText(description.get(position));
        Picasso.get().load(imageid.get(position)).fit().into(imageFlag);

        return  row;
    }
}

