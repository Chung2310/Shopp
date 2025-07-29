package com.example.shopp.ui.order;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.model.OrderDetail;
import com.example.shopp.util.Utils;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.DetailViewHolder> {
    private List<OrderDetail> list;
    private Context context;

    public OrderDetailAdapter(Context context,List<OrderDetail> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new DetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        OrderDetail detail = list.get(position);
        holder.tvTitle.setText("Tên sách: " + detail.getBook().getTitle());
        holder.tvQuantity.setText("SL: " + detail.getQuantity());
        holder.tvPrice.setText("Giá: " + detail.getUnitPrice() + " đ");

        String newUrl = Utils.BASE_URL.replace("/api/", "/");

        String postAvatarUrl = detail.getBook().getImageUrl().contains("https") ?
                detail.getBook().getImageUrl() : newUrl + "imageBook/" + detail.getBook().getImageUrl();

        Glide.with(context)
                .load(postAvatarUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.user)
                .into(holder.imgOrderDetail);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvQuantity, tvPrice;
        ImageView imgOrderDetail;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgOrderDetail = itemView.findViewById(R.id.imgOrderDetail);
        }
    }
}
