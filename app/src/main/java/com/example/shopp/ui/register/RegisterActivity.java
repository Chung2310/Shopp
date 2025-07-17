package com.example.shopp.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.shopp.databinding.ActivityRegisterBinding;
import com.example.shopp.model.LoginRequest;
import com.example.shopp.model.RegisterRequest;
import com.example.shopp.ui.login.LoginActivity;

import java.io.Serializable;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);


        registerViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {

                Intent intent = new Intent(this, LoginActivity.class);
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(user.getEmail());
                loginRequest.setPassword(binding.edtPassword.getText().toString());
                intent.putExtra("login", (Serializable) loginRequest);
                startActivity(intent);
                finish();
            }
        });
        registerViewModel.getMessageLiveData().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });


        setupEvents();

    }

    private void setupEvents() {
        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String fullName = binding.edtFullName.getText().toString().trim();
            String password = binding.edtPassword.getText().toString();
            String confirmPassword = binding.edtConfirmPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(fullName) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Không được để trống thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest request = new RegisterRequest();
            request.setEmail(email);
            request.setFullName(fullName);
            request.setPassWord(password);

            registerViewModel.createUser(request);
        });

        binding.tvGoToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

}
