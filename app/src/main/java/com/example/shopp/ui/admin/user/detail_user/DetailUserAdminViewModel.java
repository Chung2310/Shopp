package com.example.shopp.ui.admin.user.detail_user;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailUserAdminViewModel extends AndroidViewModel {
    private MutableLiveData<String> msg = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Api api;

    public DetailUserAdminViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL,getApplication()).create(Api.class);
    }

    public void deleteUser(Long userId){
        compositeDisposable.add(api.deleteUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        stringResultModel -> {
                            if(stringResultModel.getStatus() == 204){
                                msg.setValue(stringResultModel.getMessage());
                            }
                            else {
                                msg.setValue(stringResultModel.getMessage());
                            }
                        }, throwable -> {
                            Log.d("DetailUserAdminModel", throwable.getMessage());
                        }
                ));
    }

    public void setRoleAdmin(Long userId){
        compositeDisposable.add(api.setRoleAdminByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        stringResultModel -> {
                            if(stringResultModel.getStatus() == 200){
                                msg.setValue(stringResultModel.getMessage());
                            }
                            else {
                                msg.setValue(stringResultModel.getMessage());
                            }
                        }, throwable ->{
                            Log.d("DetailUserAdminModel", throwable.getMessage());
                        }
                ));
    }

    public MutableLiveData<String> getMsg() {
        return msg;
    }
}
