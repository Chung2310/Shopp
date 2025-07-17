package com.example.shopp.ui.register;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shopp.model.RegisterRequest;
import com.example.shopp.model.User;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.ui.login.LoginActivity;
import com.example.shopp.util.Utils;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegisterViewModel extends AndroidViewModel {
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> messageLiveData = new MutableLiveData<>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final Api api;

    public RegisterViewModel(@NotNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application).create(Api.class);
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<String> getMessageLiveData() {
        return messageLiveData;
    }

    public void createUser(RegisterRequest registerRequest) {
        compositeDisposable.add(api.register(registerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            Log.d("RegisterViewModel", userModel.getStatus() + "");
                            if (userModel.getStatus() == 200) {
                                userLiveData.setValue(userModel.getResult());
                                Log.d("RegisterViewModel", "User đăng ký trả về: " + (userModel.getResult() != null ? userModel.getResult().getEmail() : "null"));
                                messageLiveData.setValue(userModel.getMessage());
                            } else {
                                userLiveData.setValue(null);
                                messageLiveData.setValue(userModel.getMessage());
                            }
                        },
                        throwable -> {
                            userLiveData.setValue(null);
                            messageLiveData.setValue("Lỗi: " + throwable.getMessage());
                            Log.e("RegisterViewModel", "Error: ", throwable);
                        }
                ));
    }
}
