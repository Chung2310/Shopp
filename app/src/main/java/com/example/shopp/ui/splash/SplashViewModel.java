package com.example.shopp.ui.splash;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.shopp.model.RefreshTokenRequest;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SplashViewModel extends AndroidViewModel {
    private MutableLiveData<RefreshTokenRequest> refreshTokenRequestMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Api api;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application).create(Api.class);
    }

    public void refreshToken(RefreshTokenRequest request){
        compositeDisposable.add(api.refresh(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accessTokenModel -> {
                            Log.d("token", accessTokenModel.getResult().getAccessToken());
                            if (accessTokenModel.getStatus() == 200  ) {
                                SharedPreferences securePrefs = getApplication().getSharedPreferences("TokenAuth", Context.MODE_PRIVATE);
                                securePrefs.edit()
                                        .putString("token", accessTokenModel.getResult().getAccessToken())
                                        .putString("refreshToken", accessTokenModel.getResult().getRefreshToken())
                                        .apply();

                                refreshTokenRequestMutableLiveData.setValue(accessTokenModel.getResult());
                            } else {
                                Log.w("token", "Refresh token failed with status: " + accessTokenModel.getStatus());
                            }
                        },
                        throwable -> {
                            Log.e("token", "Refresh token error: " + throwable.getMessage(), throwable);

                        }
                ));
    }

    public MutableLiveData<RefreshTokenRequest> getRefreshTokenRequestMutableLiveData() {
        return refreshTokenRequestMutableLiveData;
    }
}
