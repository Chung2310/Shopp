package com.example.shopp.retrofit;

import com.example.shopp.model.AccessTokenModel;
import com.example.shopp.model.BookModel;
import com.example.shopp.model.CartItemRequest;
import com.example.shopp.model.CartModel;
import com.example.shopp.model.ChangePasswordRequest;
import com.example.shopp.model.ImageModel;
import com.example.shopp.model.LoginRequest;
import com.example.shopp.model.MessageModel;
import com.example.shopp.model.RefreshTokenRequest;
import com.example.shopp.model.RegisterRequest;
import com.example.shopp.model.UserModel;
import com.example.shopp.model.UserUpdateRequest;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("book")
    Observable<BookModel> getAllBook(
    );

    @GET("cart/{userId}")
    Observable<CartModel> getCartByUserId(@Path("userId") int userId);

    @PUT("cart/update")
    Observable<CartModel> updateCartItem(@Body CartItemRequest cartItemRequest);

    @DELETE("cart/remove")
    Observable<CartModel> deleteCartItem(
            @Query("userId") Long userId,
            @Query("bookId") Long bookId
    );

    @POST("cart/add")
    Observable<CartModel> addCartItem(@Body CartItemRequest cartItemRequest);

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    Observable<UserModel> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Observable<UserModel> register(@Body RegisterRequest registerRequest);

    @POST("auth/refresh")
    Observable<AccessTokenModel> refresh(@Body RefreshTokenRequest request);

    @POST("auth/update")
    Observable<UserModel> updateUser(@Body UserUpdateRequest userUpdateRequest);

    @POST("auth/changePassword")
    Observable<MessageModel> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @Multipart
    @POST("auth/uploadAvatar/{userId}")
    Observable<ImageModel> uploadImage(@Path("userId") int id,@Query("mode") String mode ,@Part MultipartBody.Part image);
}
