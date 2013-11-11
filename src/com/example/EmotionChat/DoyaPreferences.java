package com.example.EmotionChat;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * {@link android.content.SharedPreferences}のラッパと，keyに使う定数たち
 */
public class DoyaPreferences {
    private static final String PREFERENCES_NAME = "Doya";

    public static SharedPreferences getDefault(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getDefaultEditor(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
    }

    public static boolean getBooleanFromDefaultPref(
            Context context, String key, boolean defaultValue) {
        return getDefault(context).getBoolean(key, defaultValue);
    }

    public static String getPreferencesName() {
        return PREFERENCES_NAME;
    }

    public static boolean hasHachikoId(Context context) {
        return getDefault(context).contains(DoyaPreferences.KEY_MY_ID);
    }

    /**
     * @return 自身のHachikoId
     * @throws IllegalStateException HachikoIDが存在しないとき
     */
    public static long getMyHachikoId(Context context) {
        long ret =  getDefault(context).getLong(DoyaPreferences.KEY_MY_ID, -1);
        if (ret == -1) {
            throw new IllegalStateException("Hachiko ID is not found on your device");
        }
        return ret;
    }

    public static final String KEY_APP_VERSION = "app_version";
    public static final int APP_VERSION_DEFAULT = 0;
    public static final String KEY_MY_ID = "my_hachiko_id";

    public static final String KEY_USE_FAKE_REQUEST_QUEUE = "use_fake_request_queue";
    public static final boolean USE_FAKE_REQUEST_QUEUE_DEFAULT = false;

    public static final String KEY_GCM_REGISTRATION_ID = "gcm_registration_id";
    public static final String GCM_REGISTRATION_ID_DEFAULT = "";
}
