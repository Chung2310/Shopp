package com.example.shopp.ui.admin.user;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shopp.model.User;
import com.example.shopp.model.UserAdmin;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserAdminViewModel extends AndroidViewModel {
    private MutableLiveData<List<UserAdmin>> listMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Api api;

    public UserAdminViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application).create(Api.class);
    }

    public MutableLiveData<List<UserAdmin>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void getAllUserAdmin(){
        compositeDisposable.add(api.getAllUserAdmin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userAdminModel -> {
                            if(userAdminModel.getStatus() == 200){
                                listMutableLiveData.setValue(userAdminModel.getResult());
                            }
                            else {
                                Log.d("UserAdminViewModel", userAdminModel.getMessage());
                            }
                        },throwable -> {
                            Log.d("UserAdminViewModel", throwable.getMessage());
                        }
                ));
    }

    public void setRoleAdminByUserId(Long id){
        compositeDisposable.add(api.setRoleAdminByUserId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if(messageModel.getStatus() == 200){
                                Log.d("UserAdminViewModel", messageModel.getMessage());
                            }
                            else {
                                Log.d("UserAdminViewModel", messageModel.getMessage());
                            }
                        },throwable -> {
                            Log.d("UserAdminViewModel", throwable.getMessage());
                        }
                ));
    }
}