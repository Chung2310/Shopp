package com.example.shopp.ui.splash;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.shopp.R;
import com.example.shopp.activity.MainActivity;
import com.example.shopp.databinding.ActivitySplashBinding;
import com.example.shopp.model.RefreshTokenRequest;
import com.example.shopp.model.User;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.ui.login.LoginActivity;
import com.example.shopp.util.NetworkUtils;
import com.example.shopp.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private Api apiService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences securePrefs;

    // Phải giống với backend JwtTokenProvider.java
    private static final String SECRET_KEY = "jT7ZqF9YpLwXyKmBp9rQvUs4EzCeRgThJnAoSdFgHiJkLmNoPqRsTuVwXyZaBcDe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = RetrofitClient.getInstance(Utils.BASE_URL, getApplicationContext()).create(Api.class);

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            securePrefs = EncryptedSharedPreferences.create(
                    "TokenAuth",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Failed to initialize secure preferences", e);
            Toast.makeText(this, "Lỗi bảo mật", Toast.LENGTH_SHORT).show();
            return;
        }

        setupAnimation();

        new Handler().postDelayed(this::handleTokenCheck, 3000);
    }

    private void setupAnimation() {
        binding.animationView.setAnimation(R.raw.loadanimation);
        binding.animationView.playAnimation();
        binding.animationView.loop(true);

        binding.splashtext.setText("Book Shop");
        binding.splashtext.setAlpha(0f);
        binding.splashtext.animate()
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(500)
                .start();
    }

    private void handleTokenCheck() {
        SharedPreferences securePrefs = getApplicationContext().getSharedPreferences("TokenAuth", Context.MODE_PRIVATE);
        String accessToken = securePrefs.getString("token", null);
        String refreshToken = securePrefs.getString("refreshToken", null);

        Log.d("token", accessToken);

        try {
            if (accessToken != null && isTokenValid(accessToken)) {

                SharedPreferences prefs = getApplication().getSharedPreferences("UserAuth", Context.MODE_PRIVATE);
                String strUser =  prefs.getString("user", null);

                Gson gson = new Gson();
                User user = gson.fromJson(strUser, User.class);

                Utils.user = user;

                startMainActivity();

                return;
            }

            if (refreshToken != null && isTokenValid(refreshToken)) {
                refreshAccessToken(refreshToken, accessToken);
                return;
            }

            goToLogin();

        } catch (Exception e) {
            Log.e(TAG, "Token validation error", e);
            goToLogin();
        }
    }

    private boolean isTokenValid(String token) {
        try {
            byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKey key = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Log.d("JWT", "Token is valid. Subject: " + claims.getSubject());
            return true;

        } catch (SignatureException e) {
            Log.e("JWT", "Invalid token signature: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            Log.e("JWT", "Token expired: " + e.getMessage());
        } catch (MalformedJwtException | IllegalArgumentException e) {
            Log.e("JWT", "Invalid token: " + e.getMessage());
        } catch (Exception e) {
            Log.e("JWT", "Unexpected error validating token: " + e.getMessage());
        }
        return false;
    }

    private void refreshAccessToken(String refreshToken, String accessToken) {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(refreshToken);
        request.setAccessToken(accessToken);

        compositeDisposable.add(apiService.refresh(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accessTokenModel -> {
                            if (accessTokenModel.getStatus() == 200 && accessTokenModel.getResult() != null) {
                                securePrefs.edit()
                                        .putString("token", accessTokenModel.getResult().getAccessToken())
                                        .putString("refreshToken", accessTokenModel.getResult().getRefreshToken())
                                        .apply();
                                Log.d(TAG, "Token refreshed successfully");
                                startMainActivity();
                            } else {
                                Log.w(TAG, "Refresh token failed with status: " + accessTokenModel.getStatus());
                                goToLogin();
                            }
                        },
                        throwable -> {
                            Log.e(TAG, "Refresh token error: " + throwable.getMessage(), throwable);
                            showError("Không thể làm mới phiên đăng nhập");
                            goToLogin();
                        }
                ));
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goToLogin() {
        securePrefs.edit()
                .remove("token")
                .remove("refreshToken")
                .apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showError(String message) {
        binding.animationView.setAnimation(R.raw.erroranimation);
        binding.animationView.playAnimation();
        binding.animationView.loop(false);

        binding.splashtext.setText(message);
        binding.splashtext.animate()
                .alpha(1f)
                .setDuration(1500)
                .start();

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (binding.animationView != null) {
            binding.animationView.cancelAnimation();
        }

        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

        super.onDestroy();
    }
}
