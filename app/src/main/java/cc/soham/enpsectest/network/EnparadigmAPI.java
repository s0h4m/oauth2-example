package cc.soham.enpsectest.network;

import android.content.Context;

import cc.soham.enpsectest.network.model.GetResourceResponse;
import cc.soham.enpsectest.network.model.GetTokenResponse;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import timber.log.Timber;

/**
 * Created by sohammondal on 19/12/16.
 * The retrofit networking layer
 */
public class EnparadigmAPI {
    private static final String TAG = "EnparadigmAPI";
    private static EnparadigmAPIService enparadigmAPIService;
    private static final String baseUrl = "https://learn.enparadigm.com/testapi/";

    /**
     * Returns an unauthenticated API object (for getting the access token)
     * @return
     */
    public static EnparadigmAPIService getNoAuthAPI() {
        Timber.tag(TAG).d("getNoAuthAPI");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(EnParadigm.getOkHttpClient())
                .build();

        return retrofit.create(EnparadigmAPIService.class);
    }

    /**
     * Returns an authenticated API object (backed by OAUTH2)
     * @param context
     * @return
     */
    public static EnparadigmAPIService getAPI(final Context context) {
        Timber.tag(TAG).d("getAPI");
        if(enparadigmAPIService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(EnParadigm.getAuthOkHttpClient(context))
                    .build();
            enparadigmAPIService = retrofit.create(EnparadigmAPIService.class);
        }
        return enparadigmAPIService;
    }

    public interface EnparadigmAPIService {
        @GET("resource.php")
        Call<GetResourceResponse> getResource();

        @FormUrlEncoded
        @POST("token.php")
        Call<GetTokenResponse> getAccessToken(@Field("client_id") String clientId,
                                                    @Field("client_secret") String clientSecret,
                                                    @Field("grant_type") String grantType,
                                                    @Field("redirect_uri") String redirectUri,
                                                    @Field("code") String code);
    }
}
