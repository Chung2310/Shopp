package com.example.shopp.ui.review;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.shopp.model.Review;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ReviewViewModel extends AndroidViewModel {
    private MutableLiveData<List<Review>> reviewList = new MutableLiveData<>();
    private MutableLiveData<List<Long>> bookListId = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Api api;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application).create(Api.class);
    }

    public MutableLiveData<List<Review>> getReviewList() {
        return reviewList;
    }

    public MutableLiveData<List<Long>> getBookListId() {
        return bookListId;
    }

    public void createReview(Review review){
        compositeDisposable.add(api.createReview(review)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reviewModel -> {
                            if(reviewModel.getStatus() == 201){
                                reviewList.setValue(reviewModel.getResult());
                                Log.d("ReviewModel", reviewModel.getMessage());
                            }
                            else {
                                Log.d("ReviewModel", reviewModel.getMessage());
                            }
                        },throwable -> {
                            Log.d("ReviewModel", throwable.getMessage());
                        }
                ));
    }

    public void getReviewByBookId(Long bookId){
        compositeDisposable.add(api.getReviewByBookId(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reviewModel -> {
                            if(reviewModel.getStatus() == 200){
                                reviewList.setValue(reviewModel.getResult());
                                Log.d("ReviewModel", reviewModel.getMessage());
                            }
                            else {
                                Log.d("ReviewModel", reviewModel.getMessage());
                            }
                        }, throwable -> {
                            Log.d("ReviewModel", throwable.getMessage());
                        }
                ));
    }

    public void getReviewByUserId(Long userId){
        compositeDisposable.add(api.getReviewByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        reviewModel -> {
                            if(reviewModel.getStatus() == 200){
                                reviewList.setValue(reviewModel.getResult());
                            }
                            else {
                                Log.d("ReviewModel", reviewModel.getMessage());
                            }
                        },
                        throwable ->{
                            Log.d("ReviewModel", throwable.getMessage());
                        }
                ));
    }

    public void checkReview(Long userId, List<Long> bookIds){
        compositeDisposable.add(api.checkReviewed(userId, bookIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        chechReviewedModel -> {
                            if(chechReviewedModel.getStatus() == 200){
                                bookListId.setValue(chechReviewedModel.getResult());
                            }
                            else {
                                Log.d("ReviewModel", chechReviewedModel.getMessage());
                            }
                        },
                        throwable ->{
                            Log.d("ReviewModel", throwable.getMessage());
                        }
                ));
    }
}
