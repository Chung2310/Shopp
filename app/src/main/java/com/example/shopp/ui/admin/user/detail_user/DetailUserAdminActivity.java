package com.example.shopp.ui.admin.user.detail_user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.shopp.databinding.ActivityDetailUserAdminBinding;
import com.example.shopp.model.UserAdmin;
import com.example.shopp.R;
import com.example.shopp.ui.login.LoginViewModel;
import com.example.shopp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

public class DetailUserAdminActivity extends AppCompatActivity {

    private ActivityDetailUserAdminBinding binding;
    private UserAdmin userAdmin;
    private DetailUserAdminViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailUserAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(DetailUserAdminViewModel.class);

        userAdmin = (UserAdmin) getIntent().getSerializableExtra("userAdmin");

        if (userAdmin != null) {
            // Gán dữ liệu vào các TextView
            binding.textId.setText("ID: " + userAdmin.getId());
            binding.textEmail.setText("Email: " + userAdmin.getEmail());
            binding.textFullName.setText("Họ tên: " + userAdmin.getFullName());
            binding.textPhone.setText("SĐT: " + userAdmin.getPhone());
            binding.textAddress.setText("Địa chỉ: " + userAdmin.getAddress());
            binding.textRole.setText("Vai trò: " + userAdmin.getRole());
            if(Objects.equals(userAdmin.getRole(), "ROLE_ADMIN")){
                binding.btnDelete.setVisibility(View.INVISIBLE);
                binding.btnEdit.setVisibility(View.INVISIBLE);
            }
            if(userAdmin.getActive() == null) userAdmin.setActive(true);
            binding.textIsActive.setText("Kích hoạt: " + (userAdmin.getActive() ? "Đã kích hoạt" : "Chưa kích hoạt"));
            binding.textCreatedAt.setText("Tạo lúc: " + userAdmin.getCreatedAt());
            if(userAdmin.getUpdatedAt() == null){
                binding.textUpdatedAt.setText("Cập nhật: " + "chưa có update");
            }else {
                binding.textUpdatedAt.setText("Cập nhật: " + userAdmin.getUpdatedAt());
            }
            if(userAdmin.getDeleted() == null) userAdmin.setDeleted(false);
            binding.textIsDeleted.setText("Đã xóa: " + (userAdmin.getDeleted() ? "Đã Xoá" : "Chưa Xoá"));

            String newUrl = Utils.BASE_URL.replace("/api/", "/");

            String postAvatarUrl;

            if(userAdmin.getAvatarUrl() == null){
                if (userAdmin.getAvatarUrl() != null && !userAdmin.getAvatarUrl().isEmpty()) {
                    Glide.with(this)
                            .load("")
                            .placeholder(R.drawable.ic_android_black_24dp)
                            .error(R.drawable.user)
                            .into(binding.imageAvatar);
                }
                return;
            }
            else {
                postAvatarUrl = userAdmin.getAvatarUrl().contains("https") ?
                        userAdmin.getAvatarUrl() : newUrl + "avatar/" + userAdmin.getAvatarUrl();
                Log.d("AvatarUrl",postAvatarUrl);
            }

            // Load ảnh avatar nếu có
            if (userAdmin.getAvatarUrl() != null && !userAdmin.getAvatarUrl().isEmpty()) {
                Glide.with(this)
                        .load(postAvatarUrl)
                        .placeholder(R.drawable.ic_android_black_24dp)
                        .error(R.drawable.user)
                        .into(binding.imageAvatar);
            }
        }

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteUser(userAdmin.getId());
            }
        });

        viewModel.getMsg().observe(this, msg ->{
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditUserBottomSheet();
            }
        });
    }
    private void showEditUserBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_edit_user, null);
        bottomSheetDialog.setContentView(view);

        Switch switchIsActive = view.findViewById(R.id.switchIsActive);
        Switch switchIsAdmin = view.findViewById(R.id.switchIsAdmin);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        switchIsActive.setChecked(Boolean.TRUE.equals(userAdmin.getActive()));
        switchIsAdmin.setChecked("ADMIN".equalsIgnoreCase(userAdmin.getRole()));

        btnUpdate.setOnClickListener(v -> {
            userAdmin.setActive(switchIsActive.isChecked());

            userAdmin.setRole(switchIsAdmin.isChecked() ? "ADMIN" : "USER");


            bottomSheetDialog.dismiss();
            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.show();
    }

}
