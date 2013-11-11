package com.example.EmotionChat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class MainActivity extends Activity {
    private static final List<ChatItem> DUMMY_CONVERSATION = ImmutableList.<ChatItem>of(
            new ChatItem(-1, "好きです"),
            new ChatItem(1, "北海道出身です"),
            new ChatItem(-1, "好きです"),
            new ChatItem(1, "北海道出身です"),
            new ChatItem(-1, "好きです"),
            new ChatItem(1, "北海道出身です")
    );


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getActionBar().setTitle("さやかと会話");
        getActionBar().setDisplayShowHomeEnabled(false);

        final View activityRootView = findViewById(R.id.activity_root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 300) {
                    onKeyboardAppear();
                } else {
                    onKeyboardDisappear();
                }
            }
        });
        initGoogleCloudMessagingIfNecessary();

        ListView listView = (ListView) findViewById(R.id.conversation_list);
        ArrayAdapter adapter = new ChatAdapter(this, DUMMY_CONVERSATION);
        listView.setAdapter(adapter);
    }


    private void initGoogleCloudMessagingIfNecessary() {
        GoogleCloudMessagingHelper googleCloudMessagingHelper = new GoogleCloudMessagingHelper(this);
        if (!googleCloudMessagingHelper.checkPlayServices()) {
            DoyaLogger.debug("service not available");
            return;
        }
        String registrationId = googleCloudMessagingHelper.getRegistrationId(this);
        DoyaLogger.debug("regId: ", registrationId);
        if (registrationId.isEmpty()) {
            googleCloudMessagingHelper.registerInBackground(getApplicationContext());
        }
    }

    private void onKeyboardAppear() {
        findViewById(R.id.face_wrapper).setVisibility(View.GONE);
    }

    private void onKeyboardDisappear() {
        findViewById(R.id.face_wrapper).setVisibility(View.VISIBLE);
    }
}
