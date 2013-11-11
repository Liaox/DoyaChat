package com.example.EmotionChat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GoogleCloudMessagingHelper {
    private final Activity activity;
    // TODO: 実環境に合わせてここを更新
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static String SENDER_ID = "422678157380";

    public GoogleCloudMessagingHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public boolean checkPlayServices() {
        // http://developer.android.com/google/gcm/client.html からコピー
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil
                        .getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                new AlertDialog.Builder(activity)
                        .setMessage("お使いの端末では一部機能がご利用できません．お手数ですが，Doya開発チームまで問い合わせをお願いします．")
                        .show();
            }
            return false;
        }
        return true;
    }

    public String getRegistrationId(Context context) {
        SharedPreferences pref = DoyaPreferences.getDefault(activity);
        String registrationId = pref.getString(
                DoyaPreferences.KEY_GCM_REGISTRATION_ID,
                DoyaPreferences.GCM_REGISTRATION_ID_DEFAULT);
        if (registrationId.isEmpty()) {
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID since the existing
        // regID is not guaranteed to work with the new app version.
        int registeredVersion = getAppVersionCode(context);
        int currentVersion = pref.getInt(
                DoyaPreferences.KEY_APP_VERSION, DoyaPreferences.APP_VERSION_DEFAULT);
        if (registeredVersion != currentVersion) {
            DoyaLogger.info(
                    "Update from ", registeredVersion, " to ", currentVersion, " is detected.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return own app version as registered obtained by {@link android.content.pm.PackageManager}.
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void registerInBackground(final Context context) {
        final String senderId = getGcmAppId(context);
        DoyaLogger.debug(senderId);
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = "";
                try {
                    GoogleCloudMessaging googleCloudMessaging
                            = GoogleCloudMessaging.getInstance(activity);
                    String registrationId = googleCloudMessaging.register(senderId);
                    result = "device registered to GCM and ID is " + registrationId;
                    sendRegistrationIdToServer(registrationId, context);
                    storeRegistrationId(registrationId);
                } catch (IOException e) {
                    result = "Error :" + e.getMessage();
                    DoyaLogger.error("Fail to register GCM", e);
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToServer(String gcmRegistrationId, final Context context) {
        DoyaLogger.debug("sendRegistrationIdToServer");
        JSONObject params = new JSONObject();
        try {
            params.put("gcm", gcmRegistrationId);
        } catch (JSONException e) {
            DoyaLogger.error("registration_id error", e);
        }
        Request request = new JsonObjectRequest(
                DoyaAPI.REGISTER_GCM_ID.getMethod(),
                DoyaAPI.REGISTER_GCM_ID.getUrl(),
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        try {
                            DoyaLogger.debug(
                                    "Registration ID successfully sent to server. Your ID is " + obj.getLong("id"));
                            DoyaPreferences.getDefaultEditor(context).putLong(
                                    DoyaPreferences.KEY_MY_ID, obj.getLong("id"));
                        } catch (JSONException e) {
                            DoyaLogger.error("reg json parse" ,e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        DoyaLogger.error("GCM registration ID send error", volleyError);
                    }
                }
        );
        request.setRetryPolicy(DoyaAPI.RETRY_POLICY_SUPER_LONG);
        DoyaLogger.dumpRequest(request);
        DoyaApp.defaultRequestQueue().add(request);
    }

    private void storeRegistrationId(String registrationID) {
        SharedPreferences.Editor editor = DoyaPreferences.getDefaultEditor(activity);
        int appVersion = DoyaApp.getAppVersionCode();
        DoyaLogger.info("Register GCM and save registration ID on app version ", appVersion);
        editor.putInt(DoyaPreferences.KEY_APP_VERSION, appVersion)
                .putString(DoyaPreferences.KEY_GCM_REGISTRATION_ID, registrationID)
                .commit();
    }

    private String getGcmAppId(Context context) {
        Properties prop = new Properties();
        String fileName = "gcm.properties";
        try {
            InputStream fileStream = context.getAssets().open(fileName);
            prop.load(fileStream);
            fileStream.close();
        } catch (FileNotFoundException e) {
            return "GCM設定ファイルが存在しません";
        } catch (IOException e) {
            return "(取得に失敗)";
        }
        return prop.getProperty("id");
    }
}
