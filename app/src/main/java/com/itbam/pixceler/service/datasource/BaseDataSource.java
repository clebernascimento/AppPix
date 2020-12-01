package com.itbam.pixceler.service.datasource;

import com.itbam.pixceler.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseDataSource {

    protected static Retrofit retrofitBasic;
    protected static Retrofit retrofit;

    private static final String BASE_URL = BuildConfig.BASE_URL;
    private static final String BASIC_AUTH = BuildConfig.BASIC_AUTH;
    private static final String AUTH_KEY = "Authorization";

    private static final long TIMEOUT_CONNECTION_SECONDS = 10;
    private static final long SEVEN_DAYS = 60 * 60 * 24 * 7;

    public BaseDataSource() {
        createServices();
    }

    private void createServices() {
        retrofitBasic = config(BASIC_AUTH);
    }

    public void createBearerService(String token) {
        retrofit = config("Bearer " + token);
    }

    private Retrofit config(final String auth) {
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpBuilder.addInterceptor(log);
        httpBuilder.addInterceptor(setHeaders(auth));

        httpBuilder.connectTimeout(TIMEOUT_CONNECTION_SECONDS, TimeUnit.SECONDS);

        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpBuilder.build())
                .build();
    }

    private Interceptor setHeaders(String auth) {
        return chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder();
            Request request = builder.addHeader(
                    AUTH_KEY,
                    auth
            ).build();
            return chain.proceed(request);
        };
    }

}
