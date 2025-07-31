package com.example.shopp.ui.admin.user;

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
import com.example.shopp.model.User;
import com.example.shopp.util.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.UserAdminHolder> {

    private List<User> userList;
    private Context context;

    public UserAdminAdapter(List<User> userList, Context context) {
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
        User user = userList.get(position);

        String newUrl = Utils.BASE_URL.replace("/api/", "/");
        String postAvatarUrl;

        if(user.getAvatarUrl() == null){
            return;
        }
        else {
            postAvatarUrl = user.getAvatarUrl().contains("https") ?
                    user.getAvatarUrl() : newUrl + "avatar/" + user.getAvatarUrl();

        }

        Glide.with(context)
                .load(postAvatarUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.user)
                .into(holder.itemUserAdminAvatar);

        holder.itemUserAdminId.setText(user.getId());
        holder.itemUserAdminName.setText(user.getFullName());
        holder.itemUserAdminEmail.setText(user.getEmail());
        holder.itemUserAdminPhone.setText(user.getPhone());
        holder.itemUserAdminAddress.setText(user.getAddress());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserAdminHolder extends RecyclerView.ViewHolder{
        ImageView itemUserAdminAvatar;
        TextView itemUserAdminId, itemUserAdminName, itemUserAdminEmail,itemUserAdminPhone,
                itemUserAdminAddress,itemUserAdminCreatedAt,itemUserAdminUpdateAt;

        public UserAdminHolder(@NonNull View itemView) {
            super(itemView);
            itemUserAdminAvatar = itemView.findViewById(R.id.itemUserAdminAvatar);
            itemUserAdminId = itemView.findViewById(R.id.itemUserAdminId);
            itemUserAdminName = itemView.findViewById(R.id.itemUserAdminName);
            itemUserAdminEmail = itemView.findViewById(R.id.itemUserAdminEmail);
            itemUserAdminPhone = itemView.findViewById(R.id.itemUserAdminPhone);
            itemUserAdminAddress = itemView.findViewById(R.id.itemUserAdminAddress);
            itemUserAdminCreatedAt = itemView.findViewById(R.id.itemUserAdminCreatedAt);
            itemUserAdminUpdateAt = itemView.findViewById(R.id.itemUserAdminUpdateAt);
        }
    }
}
