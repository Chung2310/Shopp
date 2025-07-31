package com.example.shopp.ui.review;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.model.Review;
import com.example.shopp.util.Utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;
    private Context context;
    private OnReviewLikeListener listener;

    public ReviewAdapter(List<Review> reviews, Context context, OnReviewLikeListener listener) {
        this.reviews = reviews;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.txtReviewDate.setText(getTimeAgo(review.getCreatedAt()));
        }
        else {
            holder.txtReviewDate.setText(review.getCreatedAt());
        }
        holder.ratingBar.setRating(review.getRating());
        holder.txtReviewerName.setText(review.getUserDTO().getFullName().toString());
        holder.txtReviewContent.setText(review.getComments());

        String newUrl = Utils.BASE_URL.replace("/api/", "/");
        String avatarUrl = review.getUserDTO() != null ? review.getUserDTO().getAvatarUrl() : null;

        String postAvatarUrl;
        if (avatarUrl != null && avatarUrl.contains("https")) {
            postAvatarUrl = avatarUrl;
        } else {
            postAvatarUrl = newUrl + "avatar/" + (avatarUrl != null ? avatarUrl : "default.png");
        }

        Glide.with(context)
                .load(postAvatarUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.user)
                .into(holder.avatarItemReview);

        holder.txtHeartCount.setText(String.valueOf(review.getLikeCount()));


        boolean[] isLiked = {review.isLikedByCurrentUser()}; // dùng mảng nếu trong adapter để giữ biến mutable

        if(isLiked[0]){
            holder.imgHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.love1));
        }
        else {
            holder.imgHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.love));
        }

        holder.imgHeart.setOnClickListener(v -> {
            Animation scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_heart);
            holder.imgHeart.startAnimation(scaleAnimation);

            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isLiked[0]) {
                        // Unlike
                        holder.imgHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.love));
                    } else {
                        // Like
                        holder.imgHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.love1));
                    }
                    listener.onLikeClicked(review.getId());
                    isLiked[0] = !isLiked[0]; // toggle trạng thái
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
        });

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView txtReviewerName, txtReviewContent, txtReviewDate, txtHeartCount;
        private ImageView imgHeart;
        private RatingBar ratingBar;
        private CircleImageView avatarItemReview;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReviewContent = itemView.findViewById(R.id.txtReviewContent);
            txtReviewerName = itemView.findViewById(R.id.txtReviewerName);
            txtReviewDate = itemView.findViewById(R.id.txtReviewDate);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            avatarItemReview = itemView.findViewById(R.id.avatarItemReview);
            imgHeart = itemView.findViewById(R.id.imgHeart);
            txtHeartCount = itemView.findViewById(R.id.txtHeartCount);
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

    public interface OnReviewLikeListener {
        void onLikeClicked(Long reviewId);
    }


}

