package com.defaultapps.newsreader.data.net;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private final String BASE_URL = "https://newsapi.org/v1/";
    private final String CACHE_CONTROL = "cache_control";
    private Retrofit nonCachedRetrofit;

    @Inject
    public NetworkService() {
        nonCachedRetrofit = getNonCachedRetrofitCall();
    }

    private Retrofit getNonCachedRetrofitCall() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideCacheInterceptor())
                .build();
    }


    private Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(1, TimeUnit.DAYS)
                        .onlyIfCached()
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    public Api getNetworkCall() {
        return nonCachedRetrofit.create(Api.class);
    }
}
