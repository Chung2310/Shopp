package com.example.shopp.ui.review;

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

public class ReviewLikeViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isLiked = new MutableLiveData<>();
    private MutableLiveData<Long> countLike = new MutableLiveData<>();
    private Api api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ReviewLikeViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL,application).create(Api.class);
    }

    public MutableLiveData<Boolean> getIsLiked() {
        return isLiked;
    }

    public MutableLiveData<Long> getCountLike() {
        return countLike;
    }

    public void toggleLike(Long usedId, Long reviewId){
        compositeDisposable.add(api.toggleLike(usedId,reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        booleanMessage -> {
                            if (booleanMessage.getStatus() == 200){
                                Log.d("ReviewLikeViewModel", booleanMessage.getMessage());
                                isLiked.setValue(booleanMessage.isResult());
                            }
                            else{
                                Log.d("ReviewLikeViewModel", booleanMessage.getMessage());
                            }
                        }, throwable -> {
                            Log.d("ReviewLikeViewModel", throwable.getMessage());
                        }
                ));
    }

    public void getLikeCount(Long reviewId){
        compositeDisposable.add(api.getLikeCount(reviewId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        longModel -> {
                            if(longModel.getStatus() == 200){
                                countLike.setValue(longModel.getResult());
                                Log.d("ReviewLikeViewModel", longModel.getMessage());
                            }
                            else {
                                Log.d("ReviewLikeViewModel", longModel.getMessage());
                            }
                        }, throwable -> {
                            Log.d("ReviewLikeViewModel", throwable.getMessage());
                        }
                ));
    }

}
