package com.example.shopp.ui.review;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.databinding.ActivityReviewBinding;
import com.example.shopp.model.Book;
import com.example.shopp.model.Order;
import com.example.shopp.model.OrderDetail;
import com.example.shopp.model.Review;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.ui.order.OrderActivity;
import com.example.shopp.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private ActivityReviewBinding reviewBinding;
    private ReviewViewModel mViewmodel;
    private UserRepository userRepository;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        reviewBinding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(reviewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mViewmodel = new ViewModelProvider(this).get(ReviewViewModel.class);

        userRepository = new UserRepository(getApplicationContext());
        user = userRepository.getUser();

        String newUrl = Utils.BASE_URL.replace("/api/", "/");

        actionToolBar();

        Review review = (Review) getIntent().getSerializableExtra("reviewUpdate");
        List<String> bookTitleList = new ArrayList<>();
        if(review != null){
            bookTitleList.add(review.getBookDTO().getTitle());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    bookTitleList
            );
            reviewBinding.spinnerBooks.setAdapter(adapter);

            String imageUrl = review.getBookDTO() != null ? review.getBookDTO().getImageUrl() : null;

            String postBookUrl;
            if (imageUrl != null && imageUrl.contains("https")) {
                postBookUrl = imageUrl;
            } else {
                postBookUrl = newUrl + "image/" + (imageUrl != null ? imageUrl : "default.png");
            }

            Glide.with(getApplicationContext())
                    .load(postBookUrl)
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .error(R.drawable.user)
                    .into(reviewBinding.imageBook);

            reviewBinding.ratingBar.setRating(review.getRating());
            reviewBinding.edtComment.setText(review.getComments());

            reviewBinding.btnSubmitReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Review review1 = new Review();
                    review1.setId(review.getId());
                    review1.setBookDTO(review.getBookDTO());
                    review1.setUserDTO(review.getUserDTO());
                    review1.setRating((int) reviewBinding.ratingBar.getRating());
                    review1.setComments(reviewBinding.edtComment.getText().toString());

                    mViewmodel.updateReviewById(review1);

                    mViewmodel.getReviewList().observe(ReviewActivity.this, list -> {
                        if(list.get(0) != null){
                            Toast.makeText(getApplicationContext(),"Sửa đánh giá thành công!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HistoryReviewActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Sửa đánh giá không thành công!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else {
            Order order = (Order) getIntent().getSerializableExtra("order");

            List<Long> bookIds = new ArrayList<>();

            mViewmodel = new ViewModelProvider(this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ReviewViewModel.class);

            List<Long> allBookIds = new ArrayList<>();
            for (OrderDetail od : order.getOrderDetailDTOS()) {
                allBookIds.add((long) od.getBook().getId());
            }

            checkReviewed((long) user.getId(), allBookIds);

            mViewmodel.getBookListId().observe(this, unreviewedBookIds -> {
                bookTitleList.clear();
                bookIds.clear();

                for (OrderDetail od : order.getOrderDetailDTOS()) {
                    Long id = (long) od.getBook().getId();
                    if (unreviewedBookIds.contains(id)) {
                        bookTitleList.add(od.getBook().getTitle());
                        bookIds.add(id);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        bookTitleList
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reviewBinding.spinnerBooks.setAdapter(adapter);
            });

            reviewBinding.btnSubmitReview.setOnClickListener(v -> {
                int selectedIndex = reviewBinding.spinnerBooks.getSelectedItemPosition();

                if (selectedIndex < 0 || selectedIndex >= bookIds.size()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn sản phẩm để đánh giá", Toast.LENGTH_SHORT).show();
                    return;
                }
                Review review1 = new Review();

                review1.setUserDTO(user);

                Book book = new Book();
                book.setId(bookIds.get(selectedIndex).intValue()); // lấy đúng ID sách chưa được đánh giá
                review1.setBookDTO(book);

                review1.setComments(reviewBinding.edtComment.getText().toString());
                review1.setRating((int) reviewBinding.ratingBar.getRating());

                if (review1.getComments().trim().isEmpty() || review1.getRating() == 0) {
                    Toast.makeText(getApplicationContext(), "Thiếu thông tin yêu cầu!", Toast.LENGTH_SHORT).show();
                } else {
                    mViewmodel.createReview(review1);
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(intent);
                }
            });


        }

    }

    private void checkReviewed(Long userId, List<Long> bookIds){
        mViewmodel.checkReview(userId,bookIds);
    }

    private void actionToolBar() {
        setSupportActionBar(reviewBinding.toolBarOrder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reviewBinding.toolBarOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}