package com.example.EmotionChat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.EmotionChat.api.DoyaAPI;
import com.example.EmotionChat.api.SentTextRequest;
import com.example.EmotionChat.util.DoyaLogger;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private long friendId = 1;
    private ArrayAdapter adapter;
    private FaceView friendFace;
    private FaceView myFace;

    public static final String UPDATE_CHAT_EVENT = "update_chat";
    public static final String EXTRA_KEY_USER_ID = "user_id";
    public static final String EXTRA_KEY_CONTENT = "content";
    private BroadcastReceiver chatUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DoyaLogger.debug("on Receive", intent);
            adapter.add(new ChatItem(
                    intent.getLongExtra(EXTRA_KEY_USER_ID, -1L),
                    intent.getStringExtra(EXTRA_KEY_CONTENT)
            ));
        }
    };
    public static final String UPDATE_FACE_EVENT = "update_face";
    public static final String EXTRA_KEY_LEFT_BROW = "left_brow";
    public static final String EXTRA_KEY_RIGHT_BROW = "right_brow";
    public static final String EXTRA_KEY_LEFT_EYE = "left_eye";
    public static final String EXTRA_KEY_RIGHT_EYE = "right_eye";
    public static final String EXTRA_KEY_MOUTH = "mouth";
    private BroadcastReceiver faceUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DoyaLogger.debug("on Receive", intent);
            friendFace.setLeftBrow(intent.getIntExtra(EXTRA_KEY_LEFT_BROW, 0))
                    .setRightBrow(intent.getIntExtra(EXTRA_KEY_RIGHT_BROW, 0))
                    .setLeftEye(intent.getIntExtra(EXTRA_KEY_LEFT_EYE, 0))
                    .setRightEye(intent.getIntExtra(EXTRA_KEY_RIGHT_EYE, 0))
                    .setMouth(intent.getIntExtra(EXTRA_KEY_MOUTH, 0));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("さやかと会話");
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.main);

        friendFace = (FaceView) findViewById(R.id.face_left);
        myFace = (FaceView) findViewById(R.id.face_right);
        friendFace.setSex(FaceView.SEX.FEMALE);
        friendFace.setLeftBrow(1).setRightBrow(1).setLeftEye(1).setRightEye(1).setMouth(1);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                chatUpdateReceiver, new IntentFilter(UPDATE_CHAT_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                faceUpdateReceiver, new IntentFilter(UPDATE_FACE_EVENT));

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
        adapter = new ChatAdapter(this, new ArrayList<ChatItem>());
        listView.setAdapter(adapter);

        listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listView.setDividerHeight(1);

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = ((EditText) findViewById(R.id.editText));
                String content = editText.getText().toString();
                if (content.length() == 0) {
                    return;
                }
                Request sendRequest = new SentTextRequest(
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

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(chatUpdateReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(faceUpdateReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        friendId = DoyaPreferences.getFriendId(this);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_config:
                Intent intent = new Intent(this, MainPreferenceActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
