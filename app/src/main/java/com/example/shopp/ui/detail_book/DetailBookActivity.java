package com.example.shopp.ui.detail_book;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.databinding.ActivityDetailBookBinding;
import com.example.shopp.model.Book;
import com.example.shopp.model.Review;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.ui.cart.CartViewModel;
import com.example.shopp.ui.review.ReviewAdapter;
import com.example.shopp.ui.review.ReviewLikeViewModel;
import com.example.shopp.ui.review.ReviewViewModel;
import com.example.shopp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DetailBookActivity extends AppCompatActivity {

    private ActivityDetailBookBinding binding;
    private ReviewViewModel reviewViewModel;
    private ReviewAdapter reviewAdapter;
    private BottomSheetBehavior bottomSheetBehavior;
    private ReviewLikeViewModel reviewLikeViewModel;
    private UserRepository userRepository;
    private User user;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_book);

        binding = ActivityDetailBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        reviewLikeViewModel = new ViewModelProvider(this).get(ReviewLikeViewModel.class);

        userRepository = new UserRepository(getApplicationContext());
        user = userRepository.getUser();

        Book book = (Book) getIntent().getSerializableExtra("book");

        reviewViewModel.getReviewByBookId((long) book.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showDataDetai(book);
        }

        binding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuantityBottomSheet(book);
            }
        });

        reviewViewModel.getReviewList().observe(this, list ->{
            if(list != null){
                setReviewAdapter(list);
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetReviews);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    rotateArrow(0f, 180f); // Xoay lên thành xuống
                    isExpanded = true;
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    rotateArrow(180f, 0f); // Xoay xuống thành lên
                    isExpanded = false;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        binding.arrowUpIcon.setOnClickListener(v -> {
            if (isExpanded) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void rotateArrow(float from, float to) {
        RotateAnimation rotate = new RotateAnimation(
                from, to,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(200);
        rotate.setFillAfter(true); // Giữ trạng thái sau khi xoay
        binding.arrowUpIcon.startAnimation(rotate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showDataDetai(Book book){
        Glide.with(getApplicationContext())
                .load(book.getImageUrl())
                .error(R.drawable.ic_android_black_24dp)
                .into(binding.imgBook);
        binding.txtTitle.setText("Tên sách: " + book.getTitle());
        binding.txtAuthor.setText("Tác giả: " + book.getAuthor());
        binding.txtPublisher.setText("Nhà xuất bản: " + book.getPublisher());
        LocalDateTime dateTime = LocalDateTime.parse(book.getPublishedDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = dateTime.toLocalDate().format(formatter);
        binding.txtPublishedDate.setText("Năm xuất bản: " + formattedDate);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.txtPrice.setText("Giá: "+decimalFormat.format(Double.parseDouble(String.valueOf(book.getPrice())))+ "Đ");
        binding.txtQuantity.setText("Số lượng: "+book.getQuantity());
        binding.txtLanguage.setText("Ngôn ngữ: "+book.getLanguage());
        binding.txtGenre.setText("Thế loại: "+book.getGenre());
        binding.txtDescription.setText(book.getDescription_book());
    }

    private void showQuantityBottomSheet(Book book) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_quantity, null);
        bottomSheetDialog.setContentView(view);

        TextView tvProductName = view.findViewById(R.id.tvProductName);
        EditText etQuantity = view.findViewById(R.id.etQuantity);
        AppCompatButton btnConfirm = view.findViewById(R.id.btnConfirm);

        tvProductName.setText(book.getTitle());

        btnConfirm.setOnClickListener(v -> {
            String quantityStr = etQuantity.getText().toString().trim();
            if (quantityStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

            CartViewModel cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

            cartViewModel.addToCart( user.getId(),book.getId(),quantity);

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    public void setReviewAdapter(List<Review> reviews){
        reviewAdapter = new ReviewAdapter(reviews, getApplicationContext(), new ReviewAdapter.OnReviewLikeListener() {
            @Override
            public void onLikeClicked(Long reviewId) {
                reviewLikeViewModel.toggleLike((long) user.getId(),reviewId);
            }
        });
        binding.recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerViewReviews.setAdapter(reviewAdapter);
    }
}