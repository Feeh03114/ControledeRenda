package com.feehales.controlederenda.datas;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefences {

    private SharedPreferences mSharedPreference;

    public SharedPrefences(Context mContext){
        this.mSharedPreference = mContext.getSharedPreferences("Renda",Context.MODE_PRIVATE);

    }

    public void store(String key, Double value){
        this.mSharedPreference.edit().putString(key, String.valueOf(value)).apply();
    }

    public void storemes(String key, Integer value){
        this.mSharedPreference.edit().putString(key, String.valueOf(value)).apply();
    }
    public void check(String key, Boolean check){
        this.mSharedPreference.edit().putString(key, String.valueOf(check)).apply();
    }
    public void user (String key, String name){
        this.mSharedPreference.edit().putString(key, name).apply();
    }

    public Double getstore(String key){
        String value = this.mSharedPreference.getString(key,"0.00");
        return Double.valueOf(value);
    }

    public Integer getstoremes(String key){
        String value = this.mSharedPreference.getString(key,"-1");
        return Integer.valueOf(value);
    }

    public Boolean getcheck(String key){
        String value = this.mSharedPreference.getString(key, "false");
        return Boolean.valueOf(value);
    }
    public String getuser(String key){
        String value = this.mSharedPreference.getString(key, "");
        return value;
    }

}
