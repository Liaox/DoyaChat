package com.example.EmotionChat.push;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import com.example.EmotionChat.DoyaLogger;

/**
 * GCMを通じて送られてきたデータに応じて適切な処理をする
 */
public class GcmIntentService extends IntentService {

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

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void reportUnknownTypeNotification(Intent intent, String tag) {
        DoyaLogger.error("Unknown Intent type" + intent + tag);
    }
}
