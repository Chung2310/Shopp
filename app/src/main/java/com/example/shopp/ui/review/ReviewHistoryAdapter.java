package com.example.shopp.ui.review;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.model.Review;
import com.example.shopp.util.Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewHistoryAdapter extends RecyclerView.Adapter<ReviewHistoryAdapter.ReviewViewHistoryHolder>{

    private List<Review> reviews;
    private Context context;
    private OnReviewActionListener listener;

    public ReviewHistoryAdapter(List<Review> reviews, Context context, OnReviewActionListener listener) {
        this.reviews = reviews;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_history, parent, false);
        return new ReviewViewHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHistoryHolder holder, int position) {

        Review review = reviews.get(position);

        holder.txtReviewContent.setText(review.getComments());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.txtReviewDate.setText(getTimeAgo(review.getCreatedAt()));
        }
        else {
            holder.txtReviewDate.setText(review.getCreatedAt());
        }
        holder.ratingBar.setRating(review.getRating());
        holder.txtReviewerName.setText(review.getBookDTO().getTitle().toString());

        String imageUrl = review.getBookDTO() != null ? review.getBookDTO().getImageUrl() : null;
        String newUrl = Utils.BASE_URL.replace("/api/", "/");
        String postBookUrl;
        if (imageUrl != null && imageUrl.contains("https")) {
            postBookUrl = imageUrl;
        } else {
            postBookUrl = newUrl + "image/" + (imageUrl != null ? imageUrl : "default.png");
        }

        Glide.with(context)
                .load(postBookUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.user)
                .into(holder.avatarItemReview);


        holder.btmMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.btmMore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_review_item, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_edit) {
                        Intent intent = new Intent(context,ReviewActivity.class);
                        intent.putExtra("reviewUpdate", review);
                        holder.itemView.getContext().startActivity(intent);
                        return true;
                    } else if (item.getItemId() == R.id.action_delete) {
                        listener.onDelete(review);
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHistoryHolder extends RecyclerView.ViewHolder{
        private TextView txtReviewerName, txtReviewContent, txtReviewDate, txtHeartCount;
        private RatingBar ratingBar;
        private ImageView avatarItemReview;
        private ImageView btmMore;
        public ReviewViewHistoryHolder(@NonNull View itemView) {
            super(itemView);
            txtHeartCount = itemView.findViewById(R.id.txtHeartCount);
            txtReviewContent = itemView.findViewById(R.id.txtReviewContent);
            txtReviewerName = itemView.findViewById(R.id.txtReviewerName);
            txtReviewDate = itemView.findViewById(R.id.txtReviewDate);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            avatarItemReview = itemView.findViewById(R.id.avatarItemReview);
            btmMore = itemView.findViewById(R.id.btnMore);
        }
    }

    public static String getTimeAgo(String timeStr) {
        LocalDateTime postTime = null;
        LocalDateTime now;
        long seconds = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            postTime = LocalDateTime.parse(timeStr);
            now = LocalDateTime.now();

            Duration duration = Duration.between(postTime, now);

            seconds = duration.getSeconds();
        }

        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;

        if (seconds < 60) {
            return "vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 30) {
            return days + " ngày trước";
        } else {
            return months + " tháng trước";
        }
    }

    public interface OnReviewActionListener {
        void onDelete(Review review);
    }

}
