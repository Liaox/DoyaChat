package com.example.EmotionChat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import com.example.EmotionChat.ui.EditTextDialog;

/**
 * メイン画面から飛べる設定
 */
public class MainPreferenceActivity extends PreferenceActivity {
    private PreferenceScreen screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName(DoyaPreferences.getPreferencesName());
        screen = preferenceManager.createPreferenceScreen(this);
        Preference myId = new Preference(this);
        myId.setTitle("MyID: " + DoyaPreferences.getMyId(this));
        screen.addPreference(myId);
        screen.addPreference(friendIdPreference());
        setPreferenceScreen(screen);
    }

    private Preference friendIdPreference() {
        final Preference friendIdPref = new Preference(this);
        friendIdPref.setTitle("友達のID: " + DoyaPreferences.getFriendId(this));
        friendIdPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                EditTextDialog.showDialog(MainPreferenceActivity.this, "友達のID", "(Long)", "OK",
                        new EditTextDialog.PositiveButtonListener() {
                            @Override
                            public boolean onPositiveButtonClicked(DialogInterface dialog, String text) {
                                long friendId = Long.parseLong(text);
                                DoyaPreferences.getDefaultEditor(MainPreferenceActivity.this)
                                        .putLong(DoyaPreferences.KEY_FRIEND_ID, friendId)
                                        .commit();
                                friendIdPref.setTitle("友達のID: " + friendId);
                                return true;
                            }
                        });
                return true;
            }
        });
        return friendIdPref;
    }
}
