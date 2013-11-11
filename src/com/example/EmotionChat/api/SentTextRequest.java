package com.example.EmotionChat.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.common.collect.ImmutableMap;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 文字列を送って文字列をもらう
 */
public class SentTextRequest extends Request<String> {
    private final Response.Listener<String> listener;
    private final String param;

    public SentTextRequest(int method, String url, String param, Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.param = param;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return ImmutableMap.of("msg", param);
    }
}