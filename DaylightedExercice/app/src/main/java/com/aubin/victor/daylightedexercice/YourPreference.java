package com.aubin.victor.daylightedexercice;

import android.content.Context;
import android.content.SharedPreferences;

public class YourPreference {
    private static YourPreference yourPreference;
    private SharedPreferences sharedPreferences;

    public static YourPreference getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new YourPreference(context);
        }
        return yourPreference;
    }


    private YourPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("LikeCounters",Context.MODE_PRIVATE);
    }

    public void saveData(String key,int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public int getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getInt(key, 0);
        }
        return 0;
    }
}