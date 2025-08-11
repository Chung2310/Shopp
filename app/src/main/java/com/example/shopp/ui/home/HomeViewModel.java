package com.example.shopp.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shopp.model.Book;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class HomeViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Book>> bookList = new MutableLiveData<>();

    private Api api;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeViewModel(@NotNull Application application){
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application).create(Api.class);

    }
    public void getAllBook(int page, int size){
        compositeDisposable.add(api.getAllBook(page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bookModel -> {
                            if (bookModel.getStatus() == 200){
                                List<Book> current = bookList.getValue();
                                if (current == null) current = new ArrayList<>();
                                current.addAll(bookModel.getResult());
                                bookList.setValue(current);
                                Log.d("HomeViewModel",bookModel.getMessage());
                            }
                            else {
                                Log.d("HomeViewModel",bookModel.getMessage());
                            }
                        }
                        ,throwable -> {
                            Log.d("HomeViewModel",throwable.getMessage());
                        }
                ));
    }
    public LiveData<List<Book>> getBookList() {
        return bookList;
    }

    public void getBookListByTitle(String title){
        compositeDisposable.add(api.getBookByTitle(title)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bookModel -> {
                            if(bookModel.getStatus() == 200){
                                bookList.setValue(bookModel.getResult());
                            }
                            else {
                                Log.d("HomeViewModel", bookModel.getMessage());
                            }
                        },throwable -> {
                            Log.d("HomeViewModel", throwable.getMessage());
                        }
                ));
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear(); // cleanup
    }
}