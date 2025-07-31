package com.example.shopp.ui.cart;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shopp.model.CartItem;
import com.example.shopp.model.CartItemRequest;
import com.example.shopp.model.OrderDetail;
import com.example.shopp.model.PurchaseRequest;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartViewModel extends AndroidViewModel {
   private final MutableLiveData<List<CartItem>> cartList = new MutableLiveData<>(new ArrayList<>());
   private CompositeDisposable compositeDisposable = new CompositeDisposable();
   private Api api;
   private UserRepository userRepository;
   private User user;

    public CartViewModel(@NotNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application ).create(Api.class);
        user = userRepository.getUser();
        getCart(user.getId());
    }

    public LiveData<List<CartItem>> getCartList(){
        return cartList;
    }

    public void getCart(int id){
        compositeDisposable.add(api.getCartByUserId(id).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cartModel -> {
                            if(cartModel.getStatus() == 200){
                                cartList.setValue(cartModel.getResult());
                            }
                            else {
                                Log.d("CartViewModel", cartModel.getMessage());
                            }
                        }, throwable -> {
                            Log.d("CartViewModel", throwable.getMessage());
                        }
                ));
    }
    public void updateQuantity(int userId,int bookId, int quantity){
        CartItemRequest cartItemRequest = new CartItemRequest();
        cartItemRequest.setQuantity(quantity);
        cartItemRequest.setBookId((long) bookId);
        cartItemRequest.setUserId((long) userId);
        Log.d("CartViewModel",userId + " "+ bookId);
        compositeDisposable.add(api.updateCartItem(cartItemRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cartModel -> {
                            if(cartModel.getStatus() == 200){
                                cartList.setValue(cartModel.getResult());
                            }
                            else {
                                Log.d("CartViewModel", cartModel.getMessage());
                            }
                        },throwable -> {
                            Log.d("CartViewmodel",throwable.getMessage());
                        }
                ));
    }

    public void deleteCartItem(Long userId,Long bookId){
        Log.d("CartViewModel",bookId+"");

        compositeDisposable.add(api.deleteCartItem( userId, bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cartModel -> {
                            if(cartModel.getStatus() == 200){
                                cartList.setValue(cartModel.getResult());
                                Log.d("CartViewModel",cartModel.getMessage());
                            }
                            else {
                                Log.d("CartViewModel",cartModel.getMessage());
                            }
                        },throwable -> {
                            Log.d("CartViewModel",throwable.getMessage());
                        }
                ));
    }

    public void addToCart(int userId, int bookId, int quantity) {
        CartItemRequest request = new CartItemRequest();
        request.setUserId((long) userId);
        request.setBookId((long) bookId);
        request.setQuantity(quantity);

        compositeDisposable.add(api.addCartItem(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cartModel -> {
                            if (cartModel.getStatus() == 200) {
                                Log.d("CartViewModel", "Đã thêm vào giỏ hàng");
                                CartItem newItem = cartModel.getResult().get(0);

                                List<CartItem> currentList = cartList.getValue();
                                if (currentList == null) {
                                    currentList = new ArrayList<>();
                                }
                                cartList.setValue(currentList);
                            } else {
                                Log.d("CartViewModel", "Thêm thất bại: " + cartModel.getMessage());
                            }
                        },
                        throwable -> {
                            Log.e("CartViewModel", "Lỗi thêm giỏ hàng: " + throwable.getMessage());
                        }
                ));
    }

    public void createOrder(PurchaseRequest purchaseRequest){
        compositeDisposable.add(api.createOrder(purchaseRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if (messageModel.getStatus() == 201){
                                Log.d("CartViewModel",messageModel.getMessage());
                            }
                            else {
                                Log.d("CartViewModel",messageModel.getMessage());
                            }
                        },throwable -> {
                            Log.d("CartViewModel",throwable.getMessage());
                        }
                ));
    }

}