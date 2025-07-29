package com.example.shopp.ui.order;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shopp.model.Book;
import com.example.shopp.model.Order;
import com.example.shopp.model.OrderDetail;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrderViewModel extends AndroidViewModel {
    private MutableLiveData<List<Order>> orderListLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mesage = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Api api;
    private Book book;

    public OrderViewModel(Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL,application).create(Api.class);
    }

    public MutableLiveData<List<Order>> getOrderListLiveData() {
        return orderListLiveData;
    }

    public MutableLiveData<String> getMesage() {
        return mesage;
    }

    public void getOrderByUserId(Long userId){
        compositeDisposable.add(api.getOrderByUserId(userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        orderModel -> {
                            if (orderModel.getStatus() == 200){
                                orderListLiveData.setValue(orderModel.getResult());
                                mesage.setValue(orderModel.getMessage());

                                Gson gson = new Gson();
                                String strOrder = gson.toJson(orderModel.getResult());
                                Log.d("OrderViewModel",strOrder);
                            }
                            else {
                                mesage.setValue(orderModel.getMessage());
                                Log.d("OrderViewModel", orderModel.getMessage());
                            }
                        }, throwable -> {
                            Log.d("OrderViewModel", throwable.getMessage());
                        }
                ));
    }

}