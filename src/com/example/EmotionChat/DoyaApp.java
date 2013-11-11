package com.example.EmotionChat;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * アプリケーション全体で利用する管理するクラスなどを管理する
 */
public class DoyaApp extends Application {
    private static Context appContext;
    private static RequestQueue requestQueue;
    private static FakeRequestQueue fakeRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        requestQueue = Volley.newRequestQueue(appContext);
        requestQueue.start();
    }

    public static RequestQueue defaultRequestQueue() {
        if (DoyaPreferences.getBooleanFromDefaultPref(
                appContext,
                DoyaPreferences.KEY_USE_FAKE_REQUEST_QUEUE,
                DoyaPreferences.USE_FAKE_REQUEST_QUEUE_DEFAULT)) {
            if (fakeRequestQueue == null) {
                fakeRequestQueue = new FakeRequestQueue(appContext);
            }
            return fakeRequestQueue;
        }
        return requestQueue;
    }

    /**
     * @return own app version as registered obtained by {@link android.content.pm.PackageManager}.
     */
    public static int getAppVersionCode() {
        try {
            PackageInfo packageInfo = appContext.getPackageManager()
                    .getPackageInfo(appContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


}
