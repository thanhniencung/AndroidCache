package com.ryan.rxcache;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiennguyen on 2/26/17.
 */

public class ParseJsonUtils {
    public static <T> List<T> listEntity(String strJson, Class<T> clazz) {
        try {
            // Consuming remote method
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(strJson).getAsJsonArray();

            List<T> lst =  new ArrayList<T>();
            for(final JsonElement json: array){
                T entity = new Gson().fromJson(json, clazz);
                lst.add(entity);
            }

            return lst;

        } catch (Exception e) {
        }
        return null;
    }
}
