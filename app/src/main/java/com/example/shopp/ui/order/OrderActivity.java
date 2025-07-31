package com.example.shopp.ui.order;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shopp.R;
import com.example.shopp.databinding.ActivityOrderBinding;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.util.Utils;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private ActivityOrderBinding binding;

    private OrderViewModel orderViewModel;
    private UserRepository userRepository;
    private User user;
    private OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(getApplicationContext());
        user = userRepository.getUser();

        actionToolBar();

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderAdapter = new OrderAdapter(this, new ArrayList<>()); // ban đầu danh sách rỗng

        binding.recyclerOrder.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerOrder.setAdapter(orderAdapter);

        orderViewModel.getOrderByUserId((long) user.getId());

        orderViewModel.getOrderListLiveData().observe(this, orders -> {
            if (orders != null) {
                orderAdapter.setOrderList(getApplicationContext(),orders);
                orderAdapter.notifyDataSetChanged();
            }
        });


    }
    private void actionToolBar() {
        setSupportActionBar(binding.toolBarOrder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolBarOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}