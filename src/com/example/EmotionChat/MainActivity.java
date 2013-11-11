package com.example.EmotionChat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    String[] DUMMY_CONVERSATION = new String[] {
            "こんにちは", "さようなら", "Gracenote: 楽曲メタデータとその関連情報 (曲のムードがとれる)，流れている音楽を認識も",
            "園田、もう２限も終わってるんだぞ。遅刻の罰として放課後1人でグランド１０週だ",
            "超ジラネナイシン。先生って(39)超ＳＷ。きのう、(38)オールで、宿題やろうと思ったのにー。もー"
                    + "どのページかわからなくってー、友達の(10)ピッチにかけて聞こうとしたらー 、番号書いたメモ無くしちゃってー、"
                    + "聞けなかったからー、(49)チョバチョブ",
            "そんなのはちっとも良い訳にもならん。宿題のページが解らなかったのならきちんと早めに学校に来て、友達に聞いてやるべきじゃないのか",
            "・・・・・。日本語を話しなさい。日本語を。だいたい生活態度からしてみだれてる。なんだその眉は",
            "ていうかー。みんなアムラーとかいってー、眉抜くの当たり 前じゃん。テンプレートできれいに抜くの、結構大変だしー"
    };

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
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, DUMMY_CONVERSATION);
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
