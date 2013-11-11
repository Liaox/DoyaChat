package com.example.EmotionChat.dev;

import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.NoCache;
import com.example.EmotionChat.util.DoyaLogger;

/**
 * {@link FakeHttpStack}を利用して偽の通信結果を返す{@link com.android.volley.RequestQueue}.あちこちで適当にログを吐く．
 */
public class FakeRequestQueue extends RequestQueue {
    public FakeRequestQueue(Context context) {
        super(new NoCache(), new BasicNetwork(new FakeHttpStack(context)));
        start();
    }

    @Override
    public void start() {
        DoyaLogger.debug("request start");
        super.start();
    }

    @Override
    public void stop() {
        DoyaLogger.debug("request stop");
        super.stop();
    }

    @Override
    public Cache getCache() {
        DoyaLogger.debug("request start");
        return super.getCache();
    }

    @Override
    public void cancelAll(RequestFilter filter) {
        DoyaLogger.debug("Request cancel with filter " + filter);
        super.cancelAll(filter);
    }

    @Override
    public void cancelAll(Object tag) {
        DoyaLogger.debug("Request cancel with tag " + tag);
        super.cancelAll(tag);
    }

    @Override
    public Request add(Request request) {
        DoyaLogger.warn("Note: FakeRequestQueue is used");
        DoyaLogger.debug(
                "New request ", request.getUrl(), " is added with priority ", request.getPriority());
        try {
            if (request.getBody() == null) {
                DoyaLogger.debug("body is null");
            } else {
                DoyaLogger.debug("Body:" + new String(request.getBody()));
            }
        } catch (AuthFailureError e) {
            // cannot do anything
        }
        return super.add(request);
    }
}
