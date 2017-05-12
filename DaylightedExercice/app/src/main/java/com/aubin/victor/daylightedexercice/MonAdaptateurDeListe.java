package com.aubin.victor.daylightedexercice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    private final String[] values;
    private Context context;
    private final String[] imageUrl;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        Log.d(String.valueOf(position), values[position]);

        textView.setText(values[position]);

        Picasso.with(context).load(imageUrl[position]).into(imageView);

        return rowView;
    }

    public MonAdaptateurDeListe(Context context, String[] values, String[] imageUrl) {
        super(context, R.layout.rowlayout, values);
        this.imageUrl = imageUrl;
        this.context = context;
        this.values = values;
    }
}