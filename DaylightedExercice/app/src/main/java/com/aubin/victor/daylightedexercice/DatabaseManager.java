package com.aubin.victor.daylightedexercice;

import android.content.Context;
import android.content.SharedPreferences;

public class DatabaseManager {
    private static DatabaseManager databaseManager;
    private SharedPreferences sharedPreferences;

    public static DatabaseManager getInstance(Context context) {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager(context);
        }
        return databaseManager;
    }


    private DatabaseManager(Context context) {
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