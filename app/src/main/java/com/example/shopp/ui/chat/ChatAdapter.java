package com.example.shopp.ui.chat;

import static com.example.shopp.ui.review.ReviewHistoryAdapter.getTimeAgo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.model.Contact;
import com.example.shopp.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Contact> contactList;
    private Context context;

    public ChatAdapter(List<Contact> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.tvNameContact.setText(contact.getUserContactDTO().getFullName());
        holder.tvLastMessage.setText(shortenMessage(contact.getLastMessage()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tvLastTime.setText(getTimeAgo(contact.getLastTime()));
        }
        else {
            holder.tvLastTime.setText(contact.getLastTime());
        }

        String newUrl = Utils.BASE_URL.replace("/api/", "/");

        String postAvatarUrl;

        if(contact.getUserContactDTO().getAvatarUrl() == null){
            Glide.with(holder.itemView.getContext())
                    .load("")
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .error(R.drawable.ic_android_black_24dp)
                    .into(holder.cỉrcleImageMessage);
            return;
        }
        else {
            postAvatarUrl = contact.getUserContactDTO().getAvatarUrl().contains("https") ?
                    contact.getUserContactDTO().getAvatarUrl() : newUrl + "avatar/" + contact.getUserContactDTO().getAvatarUrl();
            Log.d("AvatarUrl",postAvatarUrl);
        }

        Glide.with(holder.itemView.getContext())
                .load(postAvatarUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.ic_android_black_24dp)
                .into(holder.cỉrcleImageMessage);

        holder.layoutContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("contact", contact);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        CircleImageView cỉrcleImageMessage;
        TextView tvNameContact, tvLastMessage, tvLastTime;
        LinearLayout layoutContact;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLastTime = itemView.findViewById(R.id.tvLastTime);
            cỉrcleImageMessage = itemView.findViewById(R.id.cỉrcleImageMessage);
            tvNameContact = itemView.findViewById(R.id.tvNameContact);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            layoutContact = itemView.findViewById(R.id.layoutContact);
        }
    }

    String shortenMessage(String message){
        int maxLength = 20;
        if (message.length() <= maxLength) {
            return message;
        }
        return message.substring(0, maxLength) + "...";
    }

}
