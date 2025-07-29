package com.example.shopp.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shopp.model.LoginRequest;
import com.example.shopp.model.User;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginViewModel extends AndroidViewModel {
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Api api;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LoginViewModel(@NotNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application).create(Api.class);
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void login(String email, String password, Context context) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        compositeDisposable.add(api.login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.getStatus() == 200) {
                                userLiveData.setValue(userModel.getResult());
                                SharedPreferences prefs = context.getSharedPreferences("TokenAuth", Context.MODE_PRIVATE);
                                prefs.edit().putString("token", userModel.getResult().getToken()).apply();
                                prefs.edit().putString("refreshToken", userModel.getResult().getRefreshToken()).apply();
                                Log.d("token","accessToken: "+userModel.getResult().getToken());
                                Log.d("token","refreshToken: "+userModel.getResult().getRefreshToken());
                            } else
                            if (userModel.getStatus() == 401)
                            {
                                userLiveData.setValue(null);
                            }
                        },
                        throwable -> {
                            Log.e("LoginViewModel", "Error: " + throwable.getMessage(), throwable);
                            userLiveData.setValue(null);
                        }
                ));
    }
}
