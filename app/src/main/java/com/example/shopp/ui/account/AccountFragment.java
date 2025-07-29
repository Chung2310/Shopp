package com.example.shopp.ui.account;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.databinding.FragmentAccountBinding;
import com.example.shopp.ui.login.LoginActivity;
import com.example.shopp.ui.order.OrderActivity;
import com.example.shopp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private FragmentAccountBinding binding;

    private AccountViewModel viewModel;

    private static final int REQUEST_CODE_PICK_IMAGE = 1001;

    private String currentUploadMode = "";

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        showDataUser();
        events();
    }

    public void events(){

        binding.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUploadMode = "avatar";
                showQuantityBottomSheet();
            }
        });

        binding.imgCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUploadMode = "background";
                showQuantityBottomSheet();
            }
        });

        binding.imgPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuantityBottomUser();
            }
        });

        binding.layoutOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                startActivity(intent);
            }
        });

        binding.layoutChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordBottomSheet();
            }
        });

        binding.layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xác nhận đăng xuất")
                        .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                        .setPositiveButton("Đăng xuất", (dialog, which) -> {
                            SharedPreferences prefs = getContext().getSharedPreferences("UserAuth", Context.MODE_PRIVATE);
                            prefs.edit().putString("user", "").apply();
                            SharedPreferences prefsUser = getContext().getSharedPreferences("TokenAuth", Context.MODE_PRIVATE);
                            prefsUser.edit().putString("token", "").apply();
                            prefsUser.edit().putString("refreshToken", "").apply();

                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();

            }
        });
    }

    public void showDataUser(){
        binding.tvEmail.setText(Utils.user.getEmail());
        binding.tvFullName.setText(Utils.user.getFullName());

        String newUrl = Utils.BASE_URL.replace("/api/", "/");

        String postBackgroundUrl,postAvatarUrl;

        if(Utils.user.getAvatarUrl() == null){
            return;
        }
        else {
            postAvatarUrl = Utils.user.getAvatarUrl().contains("https") ?
                    Utils.user.getAvatarUrl() : newUrl + "avatar/" + Utils.user.getAvatarUrl();
            Log.d("AvatarUrl",postAvatarUrl);
        }
        if(Utils.user.getBackgroundUrl() == null)
        {
            return;
        }
        else {
            postBackgroundUrl = Utils.user.getBackgroundUrl().contains("https") ?
                    Utils.user.getBackgroundUrl() : newUrl + "background/" + Utils.user.getBackgroundUrl();

            Log.d("AvatarUrl",postBackgroundUrl);
        }


        Glide.with(getContext())
                .load(postAvatarUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.user)
                .into(binding.imgAvatar);

        Glide.with(getContext())
                .load(postBackgroundUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.user)
                .into(binding.imgCover);
    }
    private void showQuantityBottomUser() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.user_bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtPhone = view.findViewById(R.id.edtPhone);
        EditText edtAddress = view.findViewById(R.id.edtAddress);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);

        edtName.setText(Utils.user.getFullName());
        edtPhone.setText(Utils.user.getPhone());
        edtAddress.setText(Utils.user.getAddress());


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPhone.getText().toString() == null ){
                    Toast.makeText(getContext(),"Số điện thoại không được trống!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if(edtName.getText().toString() == null)
                    {
                        Toast.makeText(getContext(),"Họ và tên không được trống!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        if (edtAddress.getText().toString() == null)
                        {
                            Toast.makeText(getContext(),"Địa chỉ không được trống!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                }
                viewModel.updateUser((long)Utils.user.getId(), edtName.getText().toString(), edtPhone.getText().toString(),edtAddress.getText().toString());

            }
        });

        bottomSheetDialog.show();
    }
    private void showQuantityBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_avatar, null);
        bottomSheetDialog.setContentView(view);

        TextView btsAvatar = view.findViewById(R.id.btsAvatar);
        TextView btsChooseAvatar = view.findViewById(R.id.btsChooseAvatar);

        btsAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullImageDialog(getContext(), currentUploadMode);
            }
        });

        btsChooseAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                viewModel.uploadImage(requireContext(), imageUri, currentUploadMode);
            }
        }
    }

    private void showChangePasswordBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_change_password, null);
        bottomSheetDialog.setContentView(view);

        TextInputEditText edtOldPassword =  view.findViewById(R.id.edtOldPassword);
        TextInputEditText edtNewPassword =  view.findViewById(R.id.edtNewPassword);
        TextInputEditText edtConfirmPassword =  view.findViewById(R.id.edtConfirmPassword);
        Button btnChangePassword =  view.findViewById(R.id.btnChangePassword);

        if(edtOldPassword.getText().toString() == null || edtNewPassword.getText().toString() == null || edtConfirmPassword.getText().toString() == null)
        {
            Toast.makeText(getContext(),"Không được để trống thông tin!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.changePassword((long) Utils.user.getId(), edtOldPassword.getText().toString(), edtNewPassword.getText().toString());
                }
            });
        }


        bottomSheetDialog.show();
    }

    public void showFullImageDialog(Context context,String mode) {
        String imageUrl = Utils.BASE_URL.replace("/api/", "/");
        if(mode.equals("avatar")) {
            imageUrl  = Utils.user.getAvatarUrl().contains("https") ?
                    Utils.user.getAvatarUrl() : imageUrl + "avatar/" + Utils.user.getAvatarUrl();
        }
        else {
            if(mode.equals("background") ){
                imageUrl  = Utils.user.getBackgroundUrl().contains("https") ?
                        Utils.user.getBackgroundUrl() : imageUrl + "background/" + Utils.user.getBackgroundUrl();
            }
        }

        Log.d("AvatarUrl", "Image URL: " + imageUrl);

        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_full_image);

        ImageView imageView = dialog.findViewById(R.id.imageDialog);

        Glide.with(context)
                .load(imageUrl)
                .into(imageView);

        imageView.setOnClickListener(v -> dialog.dismiss()); // ấn vào ảnh để đóng

        dialog.show();
    }

}