package com.example.shopp.ui.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.shopp.R;
import com.example.shopp.databinding.ActivityAdminBinding;
import com.example.shopp.ui.account.AccountFragment;
import com.example.shopp.ui.admin.order.OrderAdminFragment;
import com.example.shopp.ui.admin.product.ProductAdminFragment;
import com.example.shopp.ui.admin.user.UserAdminFragment;


public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserAdminFragment())
                .commit();

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_user_admin) {
                selectedFragment = new UserAdminFragment();
            } else if (id == R.id.nav_product_admin) {
                selectedFragment = new ProductAdminFragment();
            }
            else if (id == R.id.nav_order_admin) {
                selectedFragment = new OrderAdminFragment();
            }
//            else if (id == R.id.nav_account) {
//                selectedFragment = new AccountFragment();
//            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}