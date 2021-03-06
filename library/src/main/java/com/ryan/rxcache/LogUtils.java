package com.ryan.rxcache;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by kiennguyen on 2/26/17.
 */

public class LogUtils {
    protected LogUtils() {
        // Empty
    }

    private static final int VERBOSE = Log.VERBOSE;
    private static final int DEBUG = Log.DEBUG;
    private static final int INFO = Log.INFO;
    private static final int WARN = Log.WARN;
    private static final int ERROR = Log.ERROR;
    private static final int WTF = Log.ASSERT;

    // Used to identify the source of a log message.
    // It usually identifies the class or activity where the log call occurs.
    protected static String TAG = "BeeCowLog";
    // Here I prefer to use the application's packageName

    protected static void log(final int level, final String msg, final Throwable throwable) {
        final StackTraceElement[] elements = new Throwable().getStackTrace();
        String callerClassName = "?";
        String callerMethodName = "?";
        String callerLineNumber = "?";
        if (elements.length >= 4) {
            callerClassName = elements[3].getClassName();
            callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1);
            if (callerClassName.indexOf("$") > 0) {
                callerClassName = callerClassName.substring(0, callerClassName.indexOf("$"));
            }
            callerMethodName = elements[3].getMethodName();
            callerMethodName = callerMethodName.substring(callerMethodName.lastIndexOf('_') + 1);
            if (callerMethodName.equals("<init>")) {
                callerMethodName = callerClassName;
            }
            callerLineNumber = String.valueOf(elements[3].getLineNumber());
        }

        final String stack = "[" + callerClassName + "." + callerMethodName + "():" + callerLineNumber + "]" + (TextUtils.isEmpty(msg) ? "" : " ");
        switch (level) {
            case VERBOSE:
                android.util.Log.v(TAG, stack + msg, throwable);
                break ;
            case DEBUG:
                android.util.Log.d(TAG, stack + msg, throwable);
                break ;
            case INFO:
                android.util.Log.i(TAG, stack + msg, throwable);
                break ;
            case WARN:
                android.util.Log.w(TAG, stack + msg, throwable);
                break ;
            case ERROR:
                android.util.Log.e(TAG, stack + msg, throwable);
                break ;
            case WTF:
                android.util.Log.wtf(TAG, stack + msg, throwable);
                break ;
            default:
                break ;
        }
    }

    public static void d(@NonNull final String msg) {
        log(DEBUG, msg, null);
    }

    public static void d(final String msg, @NonNull final Throwable throwable) {
        log(DEBUG, msg, throwable);
    }

    public static void v(@NonNull final String msg) {
        log(VERBOSE, msg, null);
    }

    public static void v(final String msg, @NonNull final Throwable throwable) {
        log(VERBOSE, msg, throwable);
    }

    public static void i(@NonNull final String msg) {
        log(INFO, msg, null);
    }

    public static void i(final String msg, @NonNull final Throwable throwable) {
        log(INFO, msg, throwable);
    }

    public static void w(@NonNull final String msg) {
        log(WARN, msg, null);
    }

    public static void w(final String msg, @NonNull final Throwable throwable) {
        log(WARN, msg, throwable);
    }

    public static void e(@NonNull final String msg) {
        log(ERROR, msg, null);
    }

    public static void e(final String msg, @NonNull final Throwable throwable) {
        log(ERROR, msg, throwable);
    }

    public static void wtf(@NonNull final String msg) {
        log(WTF, msg, null);
    }

    public static void wtf(final String msg, @NonNull final Throwable throwable) {
        log(WTF, msg, throwable);
    }

    @Deprecated
    public static void wtf(@NonNull final Throwable throwable) {
        log(WTF, null, throwable);
    }
}
