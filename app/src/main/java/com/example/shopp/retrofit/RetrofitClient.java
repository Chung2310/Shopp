package com.example.shopp.retrofit;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static volatile Retrofit retrofit = null;
    private static final int TIMEOUT = 30; // seconds

    public static Retrofit getInstance(String baseUrl, Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new AuthInterceptor(context))
                            .addInterceptor(logging)
                            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static void clearInstance() {
        retrofit = null;
    }
}