package com.example.shopp.ui.review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.shopp.R;
import com.example.shopp.databinding.ActivityReviewBinding;
import com.example.shopp.model.Book;
import com.example.shopp.model.Order;
import com.example.shopp.model.OrderDetail;
import com.example.shopp.model.Review;
import com.example.shopp.model.User;
import com.example.shopp.ui.order.OrderActivity;
import com.example.shopp.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private ActivityReviewBinding reviewBinding;
    private ReviewViewModel mViewmodel;

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

        actionToolBar();

        Order order = (Order) getIntent().getSerializableExtra("order");

        List<Long> bookIdList = new ArrayList<>();
        List<String> bookTitleList = new ArrayList<>();

        for (OrderDetail orderDetail : order.getOrderDetailDTOS()){
            bookIdList.add( (long)orderDetail.getBook().getId());
        }

        checkReviewed( (long)Utils.user.getId(), bookIdList);

        mViewmodel.getBookListId().observe(this, reviewedBookIds -> {
            for (OrderDetail od : order.getOrderDetailDTOS()) {
                Long id = (long) od.getBook().getId();
                if (reviewedBookIds.contains(id)) {
                    bookTitleList.add(od.getBook().getTitle());
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

        mViewmodel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ReviewViewModel.class);

        mViewmodel.getReviewList().observe(this, reviews -> {
            if (reviews == null || reviews.isEmpty()) {
                Toast.makeText(getApplicationContext(),
                        "Đánh giá sản phẩm không thành công!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Đánh giá sản phẩm thành công!!", Toast.LENGTH_SHORT).show();
            }
        });

        reviewBinding.btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review review = new Review();
                User user = new User();
                user.setId(Utils.user.getId());
                review.setUserDTO(user);
                Book book = new Book();
                book.setId((int) order.getOrderDetailDTOS().get(
                        (int) reviewBinding.spinnerBooks.getSelectedItemId()).getBook().getId());

                review.setBookDTO(book);

                review.setComments(reviewBinding.edtComment.getText().toString());
                review.setRating( (int) reviewBinding.ratingBar.getRating());


                if(review.getUserDTO() == null || review.getBookDTO() == null ||
                        review.getComments().trim().isEmpty() || review.getRating() == 0){
                    Toast.makeText(getApplicationContext(),"Thiếu thông tin yêu cầu!",Toast.LENGTH_SHORT).show();
                }
                else {
                    mViewmodel.createReview(review);
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(intent);
                }
            }
        });
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