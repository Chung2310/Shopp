package com.example.shopp.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private SharedPreferences prefs;

    public AuthInterceptor(Context context) {
        prefs = context.getSharedPreferences("TokenAuth", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = prefs.getString("token", null);
        Request request = chain.request();
        if (token != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
        }
        return chain.proceed(request);
    }
}
