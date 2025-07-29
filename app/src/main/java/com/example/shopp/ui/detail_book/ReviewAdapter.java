package com.example.shopp.ui.detail_book;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.model.Review;
import com.example.shopp.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviews;
    private Context context;

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.txtReviewContent.setText(review.getComments());
        holder.txtReviewDate.setText(review.getCreatedAt());
        holder.ratingBar.setRating(review.getRating());
        holder.txtReviewerName.setText(review.getUserDTO().getFullName().toString());

        String newUrl = Utils.BASE_URL.replace("/api/", "/");
        String postAvatarUrl = review.getUserDTO().getAvatarUrl().contains("https") ?
                review.getUserDTO().getAvatarUrl() : newUrl + "avatar/" + review.getUserDTO().getAvatarUrl();

        Glide.with(context)
                .load(postAvatarUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.user)
                .into(holder.avatarItemReview);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        private TextView txtReviewerName, txtReviewContent, txtReviewDate;
        private RatingBar ratingBar;
        private CircleImageView avatarItemReview;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReviewContent = itemView.findViewById(R.id.txtReviewContent);
            txtReviewerName = itemView.findViewById(R.id.txtReviewerName);
            txtReviewDate = itemView.findViewById(R.id.txtReviewDate);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            avatarItemReview = itemView.findViewById(R.id.avatarItemReview);
        }
    }
}

