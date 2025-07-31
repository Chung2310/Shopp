package com.example.shopp.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.shopp.R;
import com.example.shopp.databinding.FragmentCartBinding;
import com.example.shopp.model.CartItem;
import com.example.shopp.model.OrderDetail;
import com.example.shopp.model.PurchaseRequest;
import com.example.shopp.model.User;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.ui.account.AccountFragment;
import com.example.shopp.ui.order.OrderActivity;
import com.example.shopp.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;
    private FragmentCartBinding binding;
    private CartAdapter adapter;
    private int total;
    private UserRepository userRepository;
    private User user;
    private List<PurchaseRequest.OrderItem> items;

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

        userRepository = new UserRepository(getContext());
        user = userRepository.getUser();

        // Setup RecyclerView
        adapter = new CartAdapter( getContext(),new ArrayList<>(), new CartAdapter.OnCartActionListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                mViewModel.updateQuantity(user.getId(), item.getBook().getId(), newQuantity);
                updateTotalPrice();
            }

            @Override
            public void onItemDeleted(CartItem item) {
                mViewModel.deleteCartItem((long) user.getId(), (long) item.getBook().getId());
                updateTotalPrice();
            }
        });
        binding.recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCart.setHasFixedSize(true);
        binding.recyclerCart.setAdapter(adapter);

        mViewModel.getCartList().observe(getViewLifecycleOwner(), cartItems -> {
            adapter.setData(cartItems);
            items = toOrderItem(cartItems);
            updateTotalPrice();
        });

        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPurchaseBottomSheet();
            }
        });
    }

    public List<PurchaseRequest.OrderItem> toOrderItem(List<CartItem> list){
        List<PurchaseRequest.OrderItem> orderItemList = new ArrayList<>();
        for (int i =0;i<list.size();i++){
            PurchaseRequest.OrderItem orderItem = new PurchaseRequest.OrderItem((long)list.get(i).getBook().getId(),list.get(i).getQuantity());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void updateTotalPrice() {
        total = 0;
        for (CartItem item : adapter.getCartItems()) {
            total += item.getBook().getPrice() * item.getQuantity();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.tvTotalPrice.setText( decimalFormat.format(total)+ "Đ");

    }

    private void showPurchaseBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_purchase, null);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        TextView etAddress =  view.findViewById(R.id.etAddress);
        TextView etPhone =  view.findViewById(R.id.etPhone);
        TextView etDescription =  view.findViewById(R.id.etDescription);
        AppCompatButton btnConfirm =  view.findViewById(R.id.btnConfirm);

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAddress.getText().toString().trim().isEmpty() || etPhone.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), "Không được để trống thông tin!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (items == null){
                        Toast.makeText(getContext(), "Không có sản phẩm nào trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PurchaseRequest purchaseRequest = new PurchaseRequest(
                            (long) user.getId(),
                            etAddress.getText().toString(),
                            etPhone.getText().toString(),
                            etDescription.getText().toString(),
                            items);

                    mViewModel.createOrder(purchaseRequest);

                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(),"Đặt hàng thành công!",Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // tránh memory leak
    }
}
