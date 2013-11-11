package com.example.EmotionChat.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;

/**
 * Class that represents information about certain API.
 */
public class DoyaAPI {
    public static final String BASE = "http://doyachat.cloudapp.net/";
    public static final RetryPolicy RETRY_POLICY_LONG
            = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    public static final RetryPolicy RETRY_POLICY_SUPER_LONG
            = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private final int method;
    private final String subUrl;

    protected DoyaAPI(int method, String subUrl) {
        this.method = method;
        this.subUrl = subUrl;
    }

    public int getMethod() {
        return method;
    }

    public String getUrl() {
        return BASE + subUrl;
    }

    public static String getSendUrl(long fromId, long toId) {
        return BASE + "send/" + Long.toString(fromId) + "/" + Long.toString(toId);
    }

    public static final DoyaAPI REGISTER_GCM_ID = new DoyaAPI(Request.Method.POST, "register");
}
