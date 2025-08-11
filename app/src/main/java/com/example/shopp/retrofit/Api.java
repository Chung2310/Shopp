package com.example.shopp.retrofit;

import com.example.shopp.model.Book;
import com.example.shopp.model.CartItem;
import com.example.shopp.model.CartItemRequest;
import com.example.shopp.model.ChangePasswordRequest;
import com.example.shopp.model.Contact;
import com.example.shopp.model.LoginRequest;
import com.example.shopp.model.Order;
import com.example.shopp.model.PurchaseRequest;
import com.example.shopp.model.RefreshTokenRequest;
import com.example.shopp.model.RegisterRequest;
import com.example.shopp.model.ResultModel;
import com.example.shopp.model.Review;
import com.example.shopp.model.User;
import com.example.shopp.model.UserAdmin;
import com.example.shopp.model.UserUpdateRequest;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
    @GET("ping")
    Observable<ResultModel<String>> ping();

    @GET("book")
    Observable<ResultModel<List<Book>>> getAllBook(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("book/{bookId}")
    Observable<ResultModel<Book>> getBookById(@Path("bookId") Long bookId);

    @GET("cart/{userId}")
    Observable<ResultModel<List<CartItem>>> getCartByUserId(@Path("userId") int userId);

    @PUT("cart/update")
    Observable<ResultModel<List<CartItem>>> updateCartItem(@Body CartItemRequest cartItemRequest);

    @DELETE("cart/remove")
    Observable<ResultModel<List<CartItem>>> deleteCartItem(
            @Query("userId") Long userId,
            @Query("bookId") Long bookId
    );

    @POST("cart/add")
    Observable<ResultModel<List<CartItem>>> addCartItem(@Body CartItemRequest cartItemRequest);

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    Observable<ResultModel<User>> login(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Observable<ResultModel<User>> register(@Body RegisterRequest registerRequest);

    @POST("auth/refreshToken")
    Observable<ResultModel<RefreshTokenRequest>> refresh(@Body RefreshTokenRequest request);

    @POST("user/update")
    Observable<ResultModel<User>> updateUser(@Body UserUpdateRequest userUpdateRequest);

    @POST("user/changePass")
    Observable<ResultModel<String>> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @Multipart
    @POST("user/uploadImage/{userId}")
    Observable<ResultModel<String>> uploadImage(
            @Path("userId") int id,
            @Query("mode") String mode,
            @Part MultipartBody.Part image
    );

    @GET("book/title")
    Observable<ResultModel<List<Book>>> getBookByTitle(@Query("title") String title);

    @GET("order/{userId}")
    Observable<ResultModel<List<Order>>> getOrderByUserId(@Path("userId") Long id);

    @POST("order")
    Observable<ResultModel<String>> createOrder(@Body PurchaseRequest purchaseRequest);

    @POST("review")
    Observable<ResultModel<List<Review>>> createReview(@Body Review review);

    @GET("review/book/{id}")
    Observable<ResultModel<List<Review>>> getReviewByBookId(@Path("id") Long bookId);

    @GET("review/user/{id}")
    Observable<ResultModel<List<Review>>> getReviewByUserId(@Path("id") Long userId);

    @POST("review/checkReviewed/{userId}")
    Observable<ResultModel<List<Long>>> checkReviewed(
            @Path("userId") Long userId,
            @Body List<Long> bookId
    );

    @DELETE("review/{id}")
    Observable<ResultModel<String>> deleteReviewById(@Path("id") Long id);

    @POST("review/update")
    Observable<ResultModel<List<Review>>> updateReviewById(@Body Review review);

    @GET("admin/users")
    Observable<ResultModel<List<UserAdmin>>> getAllUserAdmin();

    @PUT("admin/user/role/{id}")
    Observable<ResultModel<String>> setRoleAdminByUserId(@Path("id") Long id);

    @DELETE("admin/user/delete/{userId}")
    Observable<ResultModel<String>> deleteUser(@Path("userId") Long userId);

    @POST("reviewLike")
    Observable<ResultModel<Boolean>> toggleLike(@Query("userId") Long userId, @Query("reviewId") Long reviewId);

    @PUT("reviewLike")
    Observable<ResultModel<Long>> getLikeCount(@Query("reviewId") Long reviewId);

    @GET("contact/{userId}")
    Observable<ResultModel<List<Contact>>> getContactByUserId(@Path("userId") Long userId);

    @POST("contact")
    Observable<ResultModel<String>> createContact(@Body Contact contact);

    @DELETE("contact/{userId}/{userContactId}")
    Observable<ResultModel<String>> deleteContract(
            @Path("userId") Long userId,
            @Path("userContactId") Long userContactId
    );
}
