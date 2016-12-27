package cc.soham.enpsectest.network;

import android.content.Context;

import java.io.IOException;

import cc.soham.enpsectest.Values;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static cc.soham.enpsectest.network.EnParadigmTokenAuthenticator.AUTHORIZATION;

/**
 * Created by sohammondal on 26/12/16.
 * A Network {@link Interceptor} for EnParadigm Oauth operations
 * Use by adding {@link okhttp3.OkHttpClient.Builder#addNetworkInterceptor(Interceptor)} on this
 *
 * NOTE: DO NOT ADD AS a normal interceptor (this needs to be added as a network interceptor to work
 * with an {@link okhttp3.Authenticator}
 */
public class EnParadigmCompositeInterceptor implements Interceptor {
    private static final String TAG = "EnParadigmCompositeInterceptor";
    private final Context context;
    private static volatile String authToken = null;
    private static volatile String refreshToken = null;
    private static volatile String VALUE_BEARER = "Bearer %s";

    public EnParadigmCompositeInterceptor(Context context) {
        this.context = context;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        Timber.tag(TAG).d("setAuthToken:%s", authToken);
        EnParadigmCompositeInterceptor.authToken = authToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        EnParadigmCompositeInterceptor.refreshToken = refreshToken;
    }

    /**
     * Intercepts and does one of the following
     * 1. Add the access token to the query param
     * 2. Change status code of 400 to 401 for the OkHttp authenticator to work
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        // load the auth token
        initializeAuthTokenVariable();

        // execute the request
        return getResponseFromRequest(chain);
    }

    /**
     * This will execute the request, it will attach an auth token as a header in case it is there
     * @param chain
     * @return
     * @throws IOException
     */
    private Response getResponseFromRequest(Chain chain) throws IOException {
        Response response;
        if (authToken != null) {
            response = getResponseWithAuthToken(chain);
        } else {
            response = chain.proceed(chain.request());
        }
        return response;
    }

    /**
     * In case an auth token is present add the auth token to the url as a query parameter
     * @param chain
     * @return
     * @throws IOException
     */
    private Response getResponseWithAuthToken(Chain chain) throws IOException {
        Timber.tag(TAG).d("getResponseWithAuthToken adding query param authToken:%s", authToken);
        Request newRequest = chain.request().newBuilder().header(AUTHORIZATION, String.format(VALUE_BEARER, authToken)).build();
        return chain.proceed(newRequest);
    }

    /**
     * Initializes the authToken variable from Values if it hasn't been initialized before
     */
    private void initializeAuthTokenVariable() {
        Timber.tag(TAG).d("initializeAuthTokenVariable authToken:%s", authToken);
        if (authToken == null) {
            String storedAuthToken = Values.getAuthToken(context);
            if (storedAuthToken != null) {
                EnParadigmCompositeInterceptor.authToken = storedAuthToken;
            }
        }
    }
}
