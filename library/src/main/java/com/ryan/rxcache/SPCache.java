package com.ryan.rxcache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kiennguyen on 2/26/17.
 */


public class SPCache {

    private static SPCache sIntance;

    private SharedPreferences mPreferences;

    public SPCache(Context context) {
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public static SPCache init(Context context) {
        if (sIntance == null) {
            sIntance = new SPCache(context);
        }
        return sIntance;
    }

    public static SPCache getInstance() {
        return sIntance;
    }

    public void setString(String key, String value) {
        android.content.SharedPreferences.Editor edit = mPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    public void setLong(String key, long value) {
        android.content.SharedPreferences.Editor edit = mPreferences.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public long getLong(String key, long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    public void setInt(String key, int value) {
        android.content.SharedPreferences.Editor edit = mPreferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public int getInt(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    public void setBool(String key, boolean value) {
        android.content.SharedPreferences.Editor edit = mPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public boolean getBool(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }
}
