package com.example.user.testretrofit.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {
    private static LocalData localData;
    private static SharedPreferences preferences;

    public static final String SP_KEY_ACCESS_TOKEN = "access_token";

    private LocalData(){
        preferences = MyApplication.getInstance().getApplicationContext().getSharedPreferences("",Context.MODE_PRIVATE);
    }

    public static LocalData getInstance(){
        if (localData == null){
            localData = new LocalData();
        }
        return localData;
    }

    public void saveString(String key, String value){
        preferences.edit().putString(key, value).apply();
    }

    public String loadString(String key){
        return preferences.getString(key,"");
    }
}
