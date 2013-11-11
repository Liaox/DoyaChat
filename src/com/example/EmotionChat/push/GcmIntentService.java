package com.example.EmotionChat.push;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.example.EmotionChat.DoyaLogger;
import com.example.EmotionChat.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * GCMを通じて送られてきたデータに応じて適切な処理をする
 */
public class GcmIntentService extends IntentService {
    private static final String TAG_MSG = "msg";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        DoyaLogger.debug("Handle gcm intent");
        String tag = extras.getString("tag");
        String body = extras.getString("body");
        DoyaLogger.debug("Notification received: ", tag);
        DoyaLogger.debug(body);
        if (tag == null) {
            reportUnknownTypeNotification(intent, tag);
            GcmBroadcastReceiver.completeWakefulIntent(intent);
            return;
        }

        try {
            if (tag.equals(TAG_MSG)) {
                JSONObject json = new JSONObject(body);
                long id = json.getLong("from");
                String msg = json.getString("msg");
                Intent broadcastIntent = new Intent(MainActivity.UPDATE_CHAT_EVENT);
                broadcastIntent.putExtra(MainActivity.EXTRA_KEY_USER_ID, id);
                broadcastIntent.putExtra(MainActivity.EXTRA_KEY_CONTENT, msg);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                DoyaLogger.debug("updated");
            }
        } catch (JSONException e) {
            DoyaLogger.error("json parse error", e);
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void reportUnknownTypeNotification(Intent intent, String tag) {
        DoyaLogger.error("Unknown Intent type" + intent + tag);
    }
}
