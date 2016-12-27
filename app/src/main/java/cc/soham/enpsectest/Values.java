package cc.soham.enpsectest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cc.soham.enpsectest.network.EnParadigmCompositeInterceptor;
import timber.log.Timber;

/**
 * Created by sohammondal on 23/12/16.
 */

public class Values {
    private static final String TAG = "values";
    private static final String PREF_CODE = "code";
    private static final String PREF_AUTH_TOKEN = "auth_token";
    private static final String PREF_REFRESH_TOKEN = "refresh_token";

    public static void storeCode(final Context context, String code) {
        Timber.tag(TAG).d("Storing code:%s", code);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_CODE, code);
        editor.apply();
    }

    public static String getCode(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_CODE, null);
    }

    public static void storeAuthToken(final Context context, String authToken) {
        Timber.tag(TAG).d("storeAuthToken");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_AUTH_TOKEN, authToken);
        editor.apply();
        // store in AuthTokenInterceptor
        EnParadigmCompositeInterceptor.setAuthToken(authToken);
    }

    public static String getAuthToken(final Context context) {
        Timber.tag(TAG).d("getAuthToken");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_AUTH_TOKEN, null);
    }

    public static void storeRefreshToken(final Context context, String refreshToken) {
        Timber.tag(TAG).d("storeRefreshToken");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_REFRESH_TOKEN, refreshToken);
        editor.apply();
        // store in AuthTokenInterceptor
        EnParadigmCompositeInterceptor.setRefreshToken(refreshToken);
    }

    public static String getRefreshToken(final Context context) {
        Timber.tag(TAG).d("getRefreshToken");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_REFRESH_TOKEN, null);
    }
}
