package com.example.shopp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.shopp.activity.MainActivity;
import com.example.shopp.databinding.ActivityLoginBinding;
import com.example.shopp.model.LoginRequest;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.ui.admin.AdminActivity;
import com.example.shopp.ui.register.RegisterActivity;
import com.example.shopp.util.Utils;
import com.google.gson.Gson;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(LoginViewModel.class);

        userRepository = new UserRepository(getApplicationContext());

        viewModel.getMsg().observe(this, msg -> {
            if(msg != null){
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

        });

        viewModel.getUserLiveData().observe(this, user -> {
            if (user == null) {
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Log.d("LoginActivity", "User ID: " + user.getId());

                userRepository.saveUser(user);

                if(Objects.equals(user.getRole(), "ADMIN")){
                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

        Intent intent = getIntent();
        LoginRequest loginRequest = (LoginRequest) intent.getSerializableExtra("login");
        if (loginRequest != null) {
            binding.edtEmail.setText(loginRequest.getEmail());
            binding.edtPassword.setText(loginRequest.getPassword());
        }

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String pass = binding.edtPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Email và mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.login(email, pass, getApplicationContext());
            }
        });

        binding.tvRegister.setOnClickListener(v -> {
            Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerIntent);
        });
    }
}
