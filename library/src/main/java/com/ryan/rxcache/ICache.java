package com.ryan.rxcache;

import io.reactivex.Single;

/**
 * Created by kiennguyen on 2/26/17.
 */

interface ICache<T> {
    void putString(String key, String value);
    void putInt(String key, int value);
    void putLong(String key, long value);
    void putBoolean(String key, boolean value);

    String getString(String key);
    int getInt(String key);
    long getLong(String key);
    boolean getBoolean(String key);

    void putObjectToFile(String key, T data);
    void putArrayToFile(String key, T data);

    Single<T> getObjectFromFile(String key, Class<T> clazz);
    Single<T> getArrayFromFile(String key, Class<T> clazz);

    void putObjectToSharePreference(T data);
    void putArrayToToSharePreference(T data);

    Single<T> getObjectFromSharePreference(Class<T> clazz);
    Single<T> getArrayFromSharePreference(Class<T> clazz);

    boolean isCached(String key);
}
