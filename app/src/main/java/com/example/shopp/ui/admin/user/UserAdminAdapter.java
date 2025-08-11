package com.example.shopp.ui.admin.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.example.shopp.interface_click.ItemClickListener;
import com.example.shopp.model.User;
import com.example.shopp.model.UserAdmin;
import com.example.shopp.ui.admin.user.detail_user.DetailUserAdminActivity;
import com.example.shopp.util.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserAdminHolder> {

    private List<UserAdmin> userList;
    private Context context;

    public UserAdminAdapter(List<UserAdmin> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdminAdapter.UserAdminHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_admin, parent, false);
        return new UserAdminHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdminAdapter.UserAdminHolder holder, int position) {
        UserAdmin user = userList.get(position);

        String newUrl = Utils.BASE_URL.replace("/api/", "/");
        String avatarUrl = user.getAvatarUrl();

        if (avatarUrl == null || avatarUrl.isEmpty()) {
            holder.itemUserAdminAvatar.setImageResource(R.drawable.user);
        } else {
            String fullUrl = avatarUrl.contains("https") ? avatarUrl : newUrl + "avatar/" + avatarUrl;
            Glide.with(context)
                    .load(fullUrl)
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .error(R.drawable.user)
                    .into(holder.itemUserAdminAvatar);
        }

        holder.itemUserAdminName.setText("Tên: " + user.getFullName());
        holder.itemUserAdminEmail.setText("Email: " + user.getEmail());
        holder.itemUserAdminPhone.setText("SĐT: " + user.getPhone());

        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            try {
                if (user.getCreatedAt() != null) {
                    LocalDateTime createdAt = LocalDateTime.parse(user.getCreatedAt());
                    holder.itemUserAdminCreatedAt.setText("Tạo lúc: " + createdAt.format(formatter));
                }


            } catch (Exception e) {
                Log.e("UserAdminAdapter", "Lỗi định dạng thời gian: " + e.getMessage());
            }
        }

        holder.setItemClickListener(((view, pos, isLongClick) -> {
            if(!isLongClick){
                Intent intent = new Intent(holder.itemView.getContext(), DetailUserAdminActivity.class);
                intent.putExtra("userAdmin", user);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        }));
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserAdminHolder extends RecyclerView.ViewHolder{
        CircleImageView itemUserAdminAvatar;
        TextView  itemUserAdminName, itemUserAdminEmail,itemUserAdminPhone, itemUserAdminCreatedAt;
        ItemClickListener itemClickListener;

        public UserAdminHolder(@NonNull View itemView) {
            super(itemView);
            itemUserAdminAvatar = itemView.findViewById(R.id.itemUserAdminAvatar);
            itemUserAdminName = itemView.findViewById(R.id.itemUserAdminName);
            itemUserAdminEmail = itemView.findViewById(R.id.itemUserAdminEmail);
            itemUserAdminPhone = itemView.findViewById(R.id.itemUserAdminPhone);
            itemUserAdminCreatedAt = itemView.findViewById(R.id.itemUserAdminCreatedAt);
            itemView.setOnClickListener(v -> {
                if(itemClickListener != null){
                    itemClickListener.onClick(v, getAdapterPosition(),false);
                }
            });
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        public void onClick(View v){
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
}
