package com.aubin.victor.daylightedexercice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;


/**
 * Created by CTI on 12/05/2017.
 */

public class MyListAdapter extends ArrayAdapter<String> {

    private final String[] pictureNames;
    private final String[] artistNames;
    private final String[] ids;
    private Context context;
    private final String[] imageUrl;
    private HashMap<String,View> viewMap;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /* Old version
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        */

        if (viewMap.containsKey(ids[position])) {
            Log.d("reuse", ids[position]);
            return viewMap.get(ids[position]);
        }
        else {
            View rowView = LayoutInflater.from(getContext()).inflate(R.layout.rowlayout, parent, false);

            TextView textViewArtist = (TextView) rowView.findViewById(R.id.label1);
            TextView textViewName = (TextView) rowView.findViewById(R.id.label2);
            ImageButton heartButton = (ImageButton) rowView.findViewById(R.id.imageButton);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            TextView likeCounter = (TextView) rowView.findViewById(R.id.likeCounter);

            textViewName.setText("Title: " + pictureNames[position]);
            textViewArtist.setText("Artist: " + artistNames[position]);
            heartButton.setImageResource(R.drawable.heart_selected);
            heartButton.setTag(ids[position]);
            YourPreference yourPreference = YourPreference.getInstance(this.context);
            int likecount = yourPreference.getData(ids[position]);
            likeCounter.setText(String.valueOf(likecount));


            Picasso.with(context).load(imageUrl[position]).into(imageView);

            viewMap.put(ids[position], rowView);

            return rowView;
        }

    }


    public MyListAdapter(Context context, String[] pictureNames, String[] artistNames, String[] imageUrl, String[] ids) {
        super(context, R.layout.rowlayout, pictureNames);
        this.imageUrl = imageUrl;
        this.context = context;
        this.pictureNames = pictureNames;
        this.artistNames = artistNames;
        this.ids = ids;
        this.viewMap = new HashMap<String, View>();
    }
}