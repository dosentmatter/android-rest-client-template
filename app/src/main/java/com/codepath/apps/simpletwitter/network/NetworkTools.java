package com.codepath.apps.simpletwitter.network;

import android.util.Log;

import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.util.Map;

public class NetworkTools {

    private static final String TAG = NetworkTools.class.getSimpleName();

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    public static RequestParams convertToRequestParams(Map<String,
                                                       String> params) {
        RequestParams requestParams = new RequestParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestParams.put(key, value);
        }
        return requestParams;
    }
}
