package com.example.shopp.ui.detail_book;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.databinding.ActivityDetailBookBinding;
import com.example.shopp.model.Book;
import com.example.shopp.ui.cart.CartViewModel;
import com.example.shopp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class DetailBookActivity extends AppCompatActivity {

    private ActivityDetailBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_book);

        binding = ActivityDetailBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Book book = (Book) getIntent().getSerializableExtra("book");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showDataDetai(book);
        }

        binding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuantityBottomSheet(book);
            }
        });

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
        binding.txtPublishedDate.setText("Năm xuất bản: "+ dateTime.toLocalDate().toString());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.txtPrice.setText("Giá: "+decimalFormat.format(Double.parseDouble(String.valueOf(book.getPrice())))+ "Đ");
        binding.txtQuantity.setText("Số lượng: "+book.getQuantity());
        binding.txtLanguage.setText("Ngôn ngữ: "+book.getLanguage());
        binding.txtGenre.setText("Thế loại: "+book.getGenre());
        binding.txtDescription.setText("Mô tả: "+book.getDescription_book());
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

            cartViewModel.addToCart( Utils.user.getId(),book.getId(),quantity);

            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}