package cc.soham.enpsectest.network;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cc.soham.enpsectest.Values;
import cc.soham.enpsectest.network.model.GetResourceResponse;
import cc.soham.enpsectest.network.model.GetTokenResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import timber.log.Timber;

/**
 * Created by sohammondal on 19/12/16.
 * Our main networking layer exposed to the app (above the Retrofit layer and below the app)
 */
public class EnParadigm {
    private static final String TAG = "EnParadigm";

    public static final String REDIRECT_URI = "https://developers.google.com/oauthplayground";
    public static final String CLIENT_ID = "testclient";
    public static final String CLIENT_SECRET = "testpass";

    private static final String AUTH_URL = "https://learn.enparadigm.com/testapi/authorize.php?state=xyz&client_id=%s&redirect_uri=%s&response_type=code&scope=%s";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String SCOPE = "basic";

    private static final String KEY_CODE = "code";

    /**
     * Returns a call that represents a request to get an access token
     * @param code
     * @return
     */
    public static Call<GetTokenResponse> getAccessToken(String code) {
        Timber.tag(TAG).d("getAccessToken");
        return EnparadigmAPI.getNoAuthAPI().getAccessToken(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, REDIRECT_URI, code);
    }

    /**
     * Gets a token string from the EnParadigm auth API (Synchronous)
     *
     * @return
     */
    public static GetTokenResponse getNewTokenSynchronous(final Context context) throws IOException {
        Timber.tag(TAG).d("getNewTokenSynchronous");
        Call<GetTokenResponse> getAccessTokenResponseCall = EnParadigm.getAccessToken(Values.getCode(context));
        return getAccessTokenResponseCall.execute().body();
    }

    /**
     * Get an authenticated OkHttpClient (with an authenticator)
     *
     * @param context
     * @return
     */
    public static OkHttpClient getAuthOkHttpClient(final Context context) {
        Timber.tag(TAG).d("getAuthOkHttpClient");
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .authenticator(EnParadigmTokenAuthenticator.getAuthenticator(context.getApplicationContext()))
                .addNetworkInterceptor(new EnParadigmCompositeInterceptor(context))
                .addNetworkInterceptor(loggingInterceptor)
                .build();
        return okHttpClient;
    }

    /**
     * Returns a default {@link OkHttpClient} without an Authenticator
     * @return
     */
    public static OkHttpClient getOkHttpClient() {
        Timber.tag(TAG).d("getOkHttpClient");
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(loggingInterceptor).build();
        return okHttpClient;
    }

    /**
     * Handles the redirect callback after an Auth request
     *
     * @param intent
     * @return the code received from the auth response, null otherwise
     */
    public static String handleRedirect(Intent intent) {
        Timber.tag(TAG).d("handleRedirect");
        try {
            Uri data = intent.getData();
            List<String> codes = data.getQueryParameters(KEY_CODE);
            String first = codes.get(0);
            return first;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Launches a browser with a request to authenticate in case the user has not been authenticatede before
     *
     * @param appCompatActivity
     */
    public static void launchAuthUrlIfNeeded(final AppCompatActivity appCompatActivity) {
        Timber.tag(TAG).d("launchAuthUrlIfNeeded");
        if (Values.getCode(appCompatActivity) == null) {
            appCompatActivity.startActivity(getIntentForUrl(generateAuthURL()));
        }
    }

    /**
     * Generates an AUTH URL for us (based on the example
     * @return
     */
    public static String generateAuthURL() {
        return String.format(Locale.US, AUTH_URL, CLIENT_ID, REDIRECT_URI, SCOPE);
    }

    /**
     * Generates an Intent to open the given url on a web page
     * @param url
     * @return
     */
    public static Intent getIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * Get the resource (sample)
     * @param context
     * @return
     */
    public static Call<GetResourceResponse> getResource(final Context context) {
        return EnparadigmAPI.getAPI(context).getResource();
    }
}
