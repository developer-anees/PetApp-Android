package in.anees.petapp.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import in.anees.petapp.common.Constants;
import in.anees.petapp.network.api.PetAppApi;
import in.anees.petapp.utils.NetworkUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static in.anees.petapp.common.Constants.CACHE_DIR;
import static in.anees.petapp.common.Constants.CACHE_SIZE;
import static in.anees.petapp.common.Constants.MAX_AGE;
import static in.anees.petapp.common.Constants.MAX_STALE;

/**
 * Created by Anees Thyrantakath on 2019-08-03.
 */
public class PetAppNetworkService {

    private Retrofit mRetrofit;
    private Context mContext;

    public PetAppNetworkService(Context context) {
        this.mContext = context;
    }


    public PetAppApi getPetAppApi() {
        return getRetrofit().create(PetAppApi.class);
    }

    private Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().setLenient().create();

        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(getCacheClient(mContext))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .build();
        }
        return mRetrofit;
    }

    private static OkHttpClient getCacheClient(final Context context) {
        Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                if (NetworkUtils.isNetworkConnected(context)) {
                    return originalResponse
                            .newBuilder()
                            .header("Cache-Control", "public, max-age=" + MAX_AGE)
                            .build();
                } else {
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
                            .build();
                }
            }
        };

        Interceptor FORCE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                if (!NetworkUtils.isNetworkConnected(context)) {
                    builder.cacheControl(CacheControl.FORCE_CACHE);
                }
                return chain.proceed(builder.build());
            }
        };

        File httpCacheDirectory = new File(context.getCacheDir(), CACHE_DIR);
        Cache cache = new Cache(httpCacheDirectory, CACHE_SIZE);

        return new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .addInterceptor(FORCE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache)
                .build();
    }
}
