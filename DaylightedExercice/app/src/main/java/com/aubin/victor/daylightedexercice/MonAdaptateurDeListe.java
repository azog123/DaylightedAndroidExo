package com.aubin.victor.daylightedexercice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CTI on 12/05/2017.
 */

public class MonAdaptateurDeListe extends ArrayAdapter<String> {

    private final String[] pictureNames;
    private final String[] artistNames;
    private Context context;
    private final String[] imageUrl;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        TextView textViewArtist = (TextView) rowView.findViewById(R.id.label1);
        TextView textViewName = (TextView) rowView.findViewById(R.id.label2);
        ImageButton heartButton = (ImageButton) rowView.findViewById(R.id.imageButton);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textViewName.setText("Name: " + pictureNames[position]);
        textViewArtist.setText("Artist: " + artistNames[position]);

        Log.d("Tag",String.valueOf(heartButton.getTag()));
        if (!heartButton.getTag().equals("empty")){
            heartButton.setImageResource(R.drawable.heart_selected);
        }
        else {
            heartButton.setImageResource(R.drawable.heart_not_selected);
        }
        Picasso.with(context).load(imageUrl[position]).into(imageView);

        return rowView;
    }


    public MonAdaptateurDeListe(Context context, String[] pictureNames, String[] artistNames, String[] imageUrl) {
        super(context, R.layout.rowlayout, pictureNames);
        this.imageUrl = imageUrl;
        this.context = context;
        this.pictureNames = pictureNames;
        this.artistNames = artistNames;
    }
}