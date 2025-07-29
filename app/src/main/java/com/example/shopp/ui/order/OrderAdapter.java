package com.example.shopp.ui.order;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopp.R;
import com.example.shopp.databinding.ItemOrderBinding;
import com.example.shopp.model.Order;
import com.example.shopp.ui.review.ReviewActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList = new ArrayList<>();

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }


    public void setOrderList(Context context, List<Order> orders) {
        this.orderList = orders;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText("Mã đơn hàng: "+order.getId());
        holder.tvDate.setText("Ngày đặt hàng: " + order.getCreatedAt());
        holder.tvPhone.setText("Số điện thoai: "+order.getPhone());
        holder.tvAddress.setText("Địa chỉ: "+order.getAddress());
        holder.tvDescription.setText("Chú thích: "+order.getDescription());
        holder.tvOrderStatus.setText(order.getOrderStatus());

        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(context,order.getOrderDetailDTOS());

        holder.recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewOrderDetails.setAdapter(orderDetailAdapter);

        holder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("order", (Serializable) order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId,tvAddress,tvPhone,tvDescription,tvDate,tvOrderStatus;
        RecyclerView recyclerViewOrderDetails;
        AppCompatButton btnReview;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            recyclerViewOrderDetails = itemView.findViewById(R.id.recyclerViewOrderDetails);
            btnReview = itemView.findViewById(R.id.btnReview);
        }

    }
}

