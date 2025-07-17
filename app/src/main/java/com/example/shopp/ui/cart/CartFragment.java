package com.example.shopp.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shopp.databinding.FragmentCartBinding;
import com.example.shopp.model.CartItem;
import com.example.shopp.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;
    private CartAdapter adapter;
    private int total;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ViewModel
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        // Setup RecyclerView
        adapter = new CartAdapter(new ArrayList<>(), new CartAdapter.OnCartActionListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                mViewModel.updateQuantity(Utils.user.getId(), item.getBook().getId(), newQuantity);
                updateTotalPrice();
            }

            @Override
            public void onItemDeleted(CartItem item) {
                mViewModel.deleteCartItem((long) Utils.user.getId(), (long) item.getBook().getId());
                updateTotalPrice();
            }
        });
        binding.recycleViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleViewCart.setHasFixedSize(true);
        binding.recycleViewCart.setAdapter(adapter);

        mViewModel.getCartList().observe(getViewLifecycleOwner(), cartItems -> {
            adapter.setData(cartItems);
            updateTotalPrice();
        });
    }

    private void updateTotalPrice() {
        total = 0;
        for (CartItem item : adapter.getCartItems()) {
            total += item.getBook().getPrice() * item.getQuantity();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.tvTongTien.setText(decimalFormat.format(total)+ "Đ");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // tránh memory leak
    }
}
