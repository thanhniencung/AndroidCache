package com.ryan.rxcache;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kiennguyen on 2/26/17.
 */

public class AndroidCache<T> implements ICache<T> {
    public static final int CACHE_SIZE_DEFAULT = 1024 * 1024 * 10;

    private SPCache spCache;
    private DiskCache diskCache;
    private Context context;
    private Gson gson;
    private String cacheFolderName;
    private int defaultAppVersion = 1;
    private int cacheSize;

    AndroidCache(Context context, String cacheFolderName, int cacheSize) {
        this.context = context;
        this.cacheFolderName = cacheFolderName;
        this.cacheSize = cacheSize;

        init();
    }

    private void init() {
        try {
            gson = new Gson();
            File cacheFolder = null;
            if (StringUtils.isEmpty(this.cacheFolderName)) {
                cacheFolder = context.getCacheDir();
            } else {
                cacheFolder = new File(context.getCacheDir(), this.cacheFolderName);
                if (!cacheFolder.exists()) {
                    cacheFolder.mkdir();
                }
            }
            spCache = SPCache.getInstance();
            diskCache = new DiskCache(cacheFolder, defaultAppVersion, cacheSize);
        } catch (IOException exp) {
            LogUtils.e(exp.getMessage());
        }
    }

    @Override
    public void putString(String key, String value) {
        spCache.setString(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        spCache.setInt(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        spCache.setLong(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        spCache.setBool(key, value);
    }

    @Override
    public void putObjectToFile(final String key, final T data) {
        putToFile(key, data);
    }

    @Override
    public void putArrayToFile(String key, T data) {
        putToFile(key, data);
    }

    @SuppressWarnings("unchecked")
    private void putToFile(final String key, final T data) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                String requestCacheString = gson.toJson(data).toString();
                if (!StringUtils.isEmpty(requestCacheString)) {
                    diskCache.setKeyValue(key, requestCacheString);
                }
            }
        })
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                LogUtils.i("key = " + key + " cached");
            }
        });
    }

    @Override
    public Single<T> getObjectFromFile(final String key, final Class<T> clazz) {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(SingleEmitter<T> e) throws Exception {
                try {
                    String fileContent = diskCache.getValue(key);
                    if (!StringUtils.isEmpty(fileContent)) {
                        e.onSuccess(gson.fromJson(fileContent, clazz));
                    } else {
                        e.onError(new Exception("file content is empty"));
                    }
                } catch (Exception exp) {
                    e.onError(exp);
                }
            }
        });
    }

    @Override
    public Single<T> getArrayFromFile(final String key, final Class<T> clazz) {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(SingleEmitter<T> e) throws Exception {
                try {
                    String fileContent = diskCache.getValue(key);
                    e.onSuccess((T) ParseJsonUtils.listEntity(fileContent, clazz));
                } catch (Exception exp) {
                    e.onError(exp);
                }
            }
        });
    }

    private void putToSharePreference(final T data, final boolean isObject) {
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) {
                String key = data.getClass().getName();
                if (!isObject) {
                    key += ".array";
                }
                Gson gson = new Gson();
                String dataString = gson.toJson(data);
                SPCache.getInstance().setString(key, dataString);
                e.onNext(data);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                LogUtils.i("key = " + data.getClass().getName() + " cached");
            }
        });
    }

    @Override
    public void putObjectToSharePreference(T data) {
        putToSharePreference(data, true);
    }

    @Override
    public Single<T> getObjectFromSharePreference(final Class<T> clazz) {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(SingleEmitter<T> e) throws Exception {
                try {
                    String key = clazz.getName();
                    String data = SPCache.getInstance().getString(key, null);
                    if (!StringUtils.isEmpty(data)) {
                        e.onSuccess(gson.fromJson(data, clazz));
                    } else {
                        e.onError(new Exception("file content can not empty"));
                    }
                } catch (Exception exp) {
                    e.onError(exp);
                }
            }
        });
    }

    @Override
    public void putArrayToToSharePreference(T data) {
        putToSharePreference(data, false);
    }

    @Override
    public Single<T> getArrayFromSharePreference(final Class<T> clazz) {
        return Single.create(new SingleOnSubscribe<T>() {
            @Override
            public void subscribe(SingleEmitter<T> e) throws Exception {
                try {
                    String key = clazz.getName();
                    String fileContent = diskCache.getValue(key);
                    e.onSuccess((T) ParseJsonUtils.listEntity(fileContent, clazz));
                } catch (Exception exp) {
                    e.onError(exp);
                }
            }
        });
    }

    @Override
    public String getString(String key) {
        return spCache.getString(key, null);
    }

    @Override
    public int getInt(String key) {
        return spCache.getInt(key, Integer.MAX_VALUE);
    }

    @Override
    public long getLong(String key) {
        return spCache.getLong(key, Long.MAX_VALUE);
    }

    @Override
    public boolean getBoolean(String key) {
        return spCache.getBool(key, false);
    }

    @Override
    public boolean isCached(String key) {
        try {
            return diskCache.contains(key) ||
                    SPCache.getInstance().getPreferences().contains(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class Builder {
        private Context context;
        private String cacheFolder;
        private int cacheSize;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setCacheFolder(String cacheFolder) {
            this.cacheFolder = cacheFolder;
            return this;
        }

        public Builder setCacheSize(int cacheSize) {
            this.cacheSize = cacheSize > CACHE_SIZE_DEFAULT ? cacheSize : CACHE_SIZE_DEFAULT;
            return this;
        }

        public AndroidCache build() {
            return new AndroidCache(context, cacheFolder, cacheSize);
        }
    }
}
