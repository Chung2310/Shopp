package com.example.shopp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.ui.detail_book.DetailBookActivity;
import com.example.shopp.interface_click.ItemClickListener;
import com.example.shopp.model.Book;

import java.text.DecimalFormat;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private List<Book> books;
    private Context context;

    public BookAdapter(List<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = books.get(position);
        holder.tvTitle.setText("Tên sách: "+ book.getTitle());
        holder.tvAuthor.setText("Tác giả: "+book.getAuthor());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText("Giá: "+decimalFormat.format(Double.parseDouble(String.valueOf(book.getPrice())))+ "Đ");
        holder.tvDescriptionBook.setText("Mô tả: "+book.getDescription_book());

        Glide.with(context)
                .load(book.getImageUrl())
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.ic_android_black_24dp)
                .into(holder.tvImageUrl);

        holder.setItemClickListener((view, pos, isLongClick) -> {
            if (!isLongClick) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailBookActivity.class);
                intent.putExtra("book", book);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvAuthor, tvPrice,tvDescriptionBook;
        private ImageView tvImageUrl;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvDescriptionBook = itemView.findViewById(R.id.tvDescriptionBook);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvImageUrl = itemView.findViewById(R.id.tvImageUrl);
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onClick(v, getAdapterPosition(), false);
                }
            });
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
