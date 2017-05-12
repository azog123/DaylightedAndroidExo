package com.aubin.victor.daylightedexercice;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    //les views utilisées
    TextView mTxtDisplay;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mTxtDisplay = (TextView) findViewById(R.id.txtDisplay);
        getCookie();
    }


    public void getCookie(){
        volleyJsonArrayRequest(cookieUrl, cookieTag);
    }

    public void getImage() {
        volleyJsonArrayRequest(apiUrl, imageTag);
    }

    public void diplayImages(JSONArray images){
        String[] values = new String[images.length()];
        String[] imageUrl = new String[images.length()];
        for (int i = 0; i<images.length(); i++){
            try {
                values[i] = images.getJSONObject(i).getString("author_full_name");
                imageUrl[i] = images.getJSONObject(i).getString("thumbnail");
                Log.d(i+" : " + values[i],imageUrl[i]);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("BUG","");
            }
        }

        MonAdaptateurDeListe adaptateur = new MonAdaptateurDeListe(this, values, imageUrl);
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
                dialogMessage = "Downloading images ...";
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
                        mTxtDisplay.setText(e.getMessage());
                    }
                }
                else {
                    VolleyLog.d("TAG", "Error: " + error.getMessage());
                    mTxtDisplay.setText(error.getMessage());
                }
            }
        });
        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);

    }

}
