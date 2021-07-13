package com.example.bigvu_app;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//A custom list that uses Array Adapter
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

    /**
     * A method that updates the current view with the relevant components
     * @param position The position of the string in the ArrayList
     * @param convertView A view object to inflate the rows of the list
     * @param parent A viewGroup object.
     * @return The view after the components were added and the row were inflated.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflating the row component
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);
        //Initializes the components
        TextView textViewName = (TextView) row.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) row.findViewById(R.id.textviewDesc);
        ImageView imagePerson = (ImageView) row.findViewById(R.id.person_image);

        //Adding the relevant text to the components
        textViewName.setText(name.get(position));
        textViewDesc.setText(description.get(position));

        //Added ellipsize to the description
        textViewDesc.setEllipsize(TextUtils.TruncateAt.END);
        //Setting the max line number to the description component
        textViewDesc.setMaxLines(3);
        //Using the Picasso library to set the image in the ImageView from URL.
        Picasso.get().load(imageid.get(position)).fit().into(imagePerson);

        return  row;
    }
}

