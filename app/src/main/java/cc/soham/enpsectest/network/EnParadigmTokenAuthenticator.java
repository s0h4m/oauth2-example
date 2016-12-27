package cc.soham.enpsectest.network;

import android.content.Context;

import java.io.IOException;

import cc.soham.enpsectest.Values;
import cc.soham.enpsectest.network.model.GetTokenResponse;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import timber.log.Timber;

/**
 * Created by sohammondal on 19/12/16.
 * An {@link Authenticator} object that will detect 401s and generate access and refresh tokens
 */
public class EnParadigmTokenAuthenticator implements Authenticator {
    private static final String TAG = "EnParadigmTokenAuthenticator";
    public static final String AUTHORIZATION = "Authorization";

    private final Context context;

    public EnParadigmTokenAuthenticator(final Context context) {
        this.context = context;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        Timber.tag(TAG).d("authenticate");
        GetTokenResponse getTokenResponse = EnParadigm.getNewTokenSynchronous(context);
        Values.storeAuthToken(context, getTokenResponse.getAccessToken());
        Values.storeRefreshToken(context, getTokenResponse.getRefreshToken());
        return response.request().newBuilder().build();
    }

    public static Authenticator getAuthenticator(final Context context) {
        Timber.tag(TAG).d("getAuthenticator");
        return new EnParadigmTokenAuthenticator(context);
    }
}
