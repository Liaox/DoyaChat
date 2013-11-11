package com.example.EmotionChat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private long friendId = 1;

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
        final ArrayAdapter adapter = new ChatAdapter(this, new ArrayList<ChatItem>());
        listView.setAdapter(adapter);

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = ((EditText) findViewById(R.id.editText));
                String content = editText.getText().toString();
                if (content.length() == 0) {
                    return;
                }
                Request sendRequest = new StringRequest(
                        Request.Method.POST,
                        DoyaAPI.getSendUrl(getMyId(), friendId),
                        content,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                DoyaLogger.debug("content posted", s);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                DoyaLogger.error("Post Error", volleyError);
                            }
                        });
                DoyaLogger.dumpRequest(sendRequest);
                sendRequest.setRetryPolicy(DoyaAPI.RETRY_POLICY_LONG);
                DoyaApp.defaultRequestQueue().add(sendRequest);
                adapter.add(new ChatItem(getMyId(), content));
                editText.setText("");
            }
        });
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

    private long getMyId() {
        return DoyaPreferences.getMyId(this);
    }
}
