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
import androidx.lifecycle.ViewModelProvider;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.example.shopp.R;
import com.example.shopp.activity.MainActivity;
import com.example.shopp.databinding.ActivitySplashBinding;
import com.example.shopp.model.RefreshTokenRequest;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.ui.admin.AdminActivity;
import com.example.shopp.ui.login.LoginActivity;
import com.example.shopp.ui.login.LoginViewModel;
import com.example.shopp.util.NetworkUtils;
import com.example.shopp.util.Utils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

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
    private SharedPreferences securePrefs;
    private SplashViewModel viewModel;
    private UserRepository userRepository;
    private static final String SECRET_KEY = "jT7ZqF9YpLwXyKmBp9rQvUs4EzCeRgThJnAoSdFgHiJkLmNoPqRsTuVwXyZaBcDe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(SplashViewModel.class);

        userRepository = new UserRepository(getApplicationContext());

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
            Log.e(TAG, "EncryptedSharedPreferences lỗi - có thể do cài lại app", e);

            getSharedPreferences("TokenAuth", MODE_PRIVATE).edit().clear().apply();
        }

        try {
            viewModel.pingServer();

            viewModel.getStatusConnect().observe(this, ping -> {
                if(ping == 200){
                    setupAnimation();
                }
                else {
                    showError("Không thể kết nối tới server");
                }
            });
        } catch (RuntimeException e){
            showError("Không thể kết nối tới server");
        }


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
        securePrefs = getApplicationContext().getSharedPreferences("TokenAuth", Context.MODE_PRIVATE);
        String accessToken = securePrefs.getString("token", null);
        String refreshToken = securePrefs.getString("refreshToken", null);

        Log.d("token","accessToken: " + accessToken);
        Log.d("token","refreshToken: " + refreshToken);

        try {
            boolean isAccessTokenValid = accessToken != null && isTokenValid(accessToken);
            boolean isRefreshTokenValid = refreshToken != null && isTokenValid(refreshToken);

            if (isAccessTokenValid) {
                Log.d("token", "✅ accessToken hợp lệ, tiếp tục vào app");

                User user = userRepository.getUser();

                if (user != null) {
                    if(Objects.equals(user.getRole(), "ROLE_ADMIN")){
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                        return;
                    }
                    startMainActivity();
                } else {
                    Log.d("token", "User không có dữ liệu!");
                }

            } else if (isRefreshTokenValid) {
                Log.d("token", "⚠ accessToken hết hạn, dùng refreshToken để lấy mới");
                refreshAccessToken(refreshToken, accessToken);
            } else {
                Log.d("token", "❌ Cả accessToken và refreshToken đều hết hạn, yêu cầu đăng nhập lại");
                goToLogin();
            }

        } catch (Exception e) {
            Log.e("token", "❌ Lỗi khi kiểm tra token: " + e.getMessage());
            goToLogin();
        }
    }


    private boolean isTokenValid(String token) {
        Log.d("JWT", "==== Bắt đầu xét token ====");
        Log.d("JWT", "Token nhận được: " + token);

        try {
            byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            Log.d("JWT", "SECRET_KEY bytes length: " + secretKeyBytes.length);

            SecretKey key = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
            Log.d("JWT", "Đã tạo secret key cho giải mã");

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Log.d("JWT", "Giải mã thành công token!");
            Log.d("JWT", "Subject: " + claims.getSubject());
            Log.d("JWT", "Issued at: " + claims.getIssuedAt());
            Log.d("JWT", "Expiration: " + claims.getExpiration());
            Log.d("JWT", "Roles: " + claims.get("roles")); // Nếu bạn có custom claim

            Log.d("JWT", "==== Token hợp lệ ====");
            return true;

        } catch (ExpiredJwtException e) {
            Log.e("JWT", "⚠ Token đã hết hạn: " + e.getMessage());
            Log.e("JWT", "Expired at: " + e.getClaims().getExpiration());
        } catch (SignatureException e) {
            Log.e("JWT", "❌ Chữ ký token không hợp lệ: " + e.getMessage());
        } catch (MalformedJwtException e) {
            Log.e("JWT", "❌ Token không đúng định dạng: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.e("JWT", "❌ Token rỗng hoặc không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            Log.e("JWT", "❌ Lỗi không xác định khi xét token: " + e.getMessage());
        }

        Log.d("JWT", "==== Token không hợp lệ ====");
        return false;
    }


    private void refreshAccessToken(String refreshToken, String accessToken) {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(refreshToken);
        request.setAccessToken(accessToken);

        viewModel.refreshToken(request);

        viewModel.getRefreshTokenRequestMutableLiveData().observe(this, response -> {
            if (response != null) {
                startMainActivity();
            } else {
                goToLogin();
            }
        });
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

        super.onDestroy();
    }
}
