package com.arena.gifttobuddy.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static SharedPreference yourPreference;
    private SharedPreferences sharedPreferences;

    public static SharedPreference getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new SharedPreference(context);
        }
        return yourPreference;
    }

    private SharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences("UserData",Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public void saveDataLogin(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getDataLogin(String key){
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "0");
        }
        return "";
    }

    public void saveNotification(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getNotification(String key){
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "0");
        }
        return "";
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "0");
        }
        return "";
    }
}
