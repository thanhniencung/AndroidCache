package com.ryan.rxcache;

/**
 * Created by kiennguyen on 2/26/17.
 */

public class StringUtils {
    public static boolean isEmpty(String input) {
        if(input == null ||
                (input != null && input.trim().length() == 0)){
            return true;
        }
        return false;
    }
}
