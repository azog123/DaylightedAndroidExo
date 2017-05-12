package com.aubin.victor.daylightedexercice;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends ListActivity {

    //les urls utilisés
    String cookieUrl = "https://www.daylighted.com/demo/business";
    String apiUrl = "https://www.daylighted.com/corporate/collection/add/1d85fb9e2f5511e78b047054d219b30e?format=json&f=71&p=1";

    //les tags utilisés
    final String cookieTag = "getCookie";
    final String imageTag = "getImage";

    //le player
    MediaPlayer mPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCookie();
    }


    public void getCookie(){
        volleyJsonArrayRequest(cookieUrl, cookieTag);
    }

    public void getImage() {
        volleyJsonArrayRequest(apiUrl, imageTag);
    }

    public void diplayImages(JSONArray images){
        String[] artistNames = new String[images.length()];
        String[] imageUrl = new String[images.length()];
        String[] pictureNames = new String[images.length()];
        String[] ids = new String[images.length()];
        for (int i = 0; i<images.length(); i++){
            try {
                artistNames[i] = images.getJSONObject(i).getString("author_full_name");
                imageUrl[i] = images.getJSONObject(i).getString("thumbnail");
                pictureNames[i] = images.getJSONObject(i).getString("name");
                ids[i] = images.getJSONObject(i).getString("uid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        MyListAdapter adaptateur = new MyListAdapter(this, pictureNames, artistNames, imageUrl, ids);
        setListAdapter(adaptateur);
    }


    /**
     * Cette méthode effectue une requete à l'url.
     * Si elle récupère un JSON Array, la procédure de traitement d'image est lancée.
     * Sinon on récupère le cookie et on fait l'appel à l'API
     * @param url
     * @param tag
     */
    public void volleyJsonArrayRequest(String url, String tag ){

        final String  REQUEST_TAG = tag;
        final String dialogMessage;

        //on affiche un message d'attente personalisé
        switch (tag){
            case cookieTag:
                dialogMessage = "Connecting...";
                break;
            case imageTag:
                dialogMessage = "Downloading pictures ...";
                break;
            default:
                dialogMessage = "Loading. Please wait...";
        }

        final ProgressDialog dialog = ProgressDialog.show(this, "",
                dialogMessage, true);

        JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    //cette méthode esy appelé lorsque l'appel à l'API s'est bien déroulé
                    public void onResponse(JSONArray response) {
                        dialog.hide();
                        Toast toast = Toast.makeText(getApplicationContext(), "Displaying pictures...", Toast.LENGTH_SHORT);
                        toast.show();
                        diplayImages(response);
                    }
                },
                new Response.ErrorListener() {
            @Override
            //cette méthode est appelée lorsqu'on va chercher le cookie ou lorsque l'appel à l'API ne s'est pas bien déroulé
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                if (REQUEST_TAG.equals(cookieTag)) {
                    try {
                        error.networkResponse.headers.get("Set-Cookie");
                        //le cookie est stocké automatiquement
                        Toast toast = Toast.makeText(getApplicationContext(), "Connection succeed !", Toast.LENGTH_SHORT);
                        toast.show();
                        getImage();
                    }
                    catch (Exception e){
                        Log.d("getCookie", e.getMessage());
                        Toast toast = Toast.makeText(getApplicationContext(), "Connection failed, check your network and restart the app.",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else {
                    VolleyLog.d("API Fetching", "Error: " + error.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "API unreachable, please check your network and restart the app.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);

    }

    public void heartClick(View heartView){

        if (heartView.getTag().equals("empty")){

            if(mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
            }
            mPlayer = MediaPlayer.create(this, R.raw.buttonclick);
            mPlayer.start();

            ImageButton heartButton = (ImageButton) heartView;
            heartButton.setImageResource(R.drawable.heart_selected);
            heartButton.setTag("full");
        }
        else {
            ImageButton heartButton = (ImageButton) heartView;
            heartButton.setImageResource(R.drawable.heart_not_selected);
            heartButton.setTag("empty");
        }
    }


}
