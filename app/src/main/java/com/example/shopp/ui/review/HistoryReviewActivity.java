package com.example.shopp.ui.review;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shopp.R;
import com.example.shopp.databinding.ActivityHistoryReviewBinding;
import com.example.shopp.model.Review;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.util.Utils;

import java.util.List;

public class HistoryReviewActivity extends AppCompatActivity {

    private ActivityHistoryReviewBinding historyReviewBinding;
    private ReviewViewModel reviewViewModel;
    private ReviewHistoryAdapter reviewHistoryAdapter;
    private UserRepository userRepository;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        historyReviewBinding = ActivityHistoryReviewBinding.inflate(getLayoutInflater());
        setContentView(historyReviewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        userRepository = new UserRepository(getApplicationContext());

        user = userRepository.getUser();

        actionToolBar();

        reviewViewModel.getReviewByUserId((long) user.getId());

        reviewViewModel.getReviewList().observe(this, listReview -> {
            setAdapter(listReview);
        });


    }

    private void setAdapter(List<Review> reviews) {
        reviewHistoryAdapter = new ReviewHistoryAdapter(reviews, getApplicationContext(), new ReviewHistoryAdapter.OnReviewActionListener() {
            @Override
            public void onDelete(Review review) {
                reviewViewModel.deleteReviewById(review.getId());
                reviewViewModel.getMsg().observe(HistoryReviewActivity.this, msgg ->{
                    if(msgg != null){
                        Toast.makeText(getApplicationContext(), msgg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        historyReviewBinding.recyclerHistoryReview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        historyReviewBinding.recyclerHistoryReview.setAdapter(reviewHistoryAdapter);
    }

    private void actionToolBar() {
        setSupportActionBar(historyReviewBinding.toolBarHistoryReview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        historyReviewBinding.toolBarHistoryReview.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}