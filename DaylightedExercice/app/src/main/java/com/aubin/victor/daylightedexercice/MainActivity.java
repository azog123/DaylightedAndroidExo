package com.aubin.victor.daylightedexercice;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
        for (int i = 0; i<images.length(); i++){
            try {
                artistNames[i] = images.getJSONObject(i).getString("author_full_name");
                imageUrl[i] = images.getJSONObject(i).getString("thumbnail");
                pictureNames[i] = images.getJSONObject(i).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        MonAdaptateurDeListe adaptateur = new MonAdaptateurDeListe(this, pictureNames, artistNames, imageUrl);
        setListAdapter(adaptateur);
    }


    public void heartClick(View heartView){
        if (heartView.getTag().equals("empty")){
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
                        Log.d("API VOLLEY", response.toString());
                        Log.d("API VOLLEY", String.valueOf(response.length()));
                        dialog.hide();

                        Context context = getApplicationContext();
                        CharSequence text = "Affichage des images";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        diplayImages(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            //cette méthode est appelée lorsqu'on va chercher le cookie ou lorsque l'appel à l'API ne s'est pas bien déroulé
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                if (REQUEST_TAG.equals(cookieTag)) {
                    try {
                        String.valueOf(error.networkResponse.headers.get("Set-Cookie"));
                        //le cookie est stocké automatiquement
                        Context context = getApplicationContext();
                        CharSequence text = "Connexion réussie !";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        getImage();
                    }
                    catch (Exception e){
                        //mTxtDisplay.setText(e.getMessage());
                    }
                }
                else {
                    VolleyLog.d("TAG", "Error: " + error.getMessage());
                    //mTxtDisplay.setText(error.getMessage());
                }
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);

    }


}
