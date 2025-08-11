package com.example.shopp.ui.home;

import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.databinding.FragmentHomeBinding;
import com.example.shopp.model.Book;
import com.example.shopp.ui.chat.ChatFragment;
import com.example.shopp.util.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel mViewModel;
    private BookAdapter bookAdapter;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private List<Book> fullBookList = new ArrayList<>();
    int size =10;
    int page = 0;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mViewModel.getAllBook(page,size);

        mViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            isLoading = false;
            if ((books.size() < size)){
                isLastPage = true;
            }

            fullBookList = books;
            setBookAdapter();
        });







        String[] banners = {
                "https://thietkelogo.edu.vn/uploads/images/thiet-ke-do-hoa-khac/banner-sach/3.jpg",
                "https://thietkelogo.edu.vn/uploads/images/thiet-ke-do-hoa-khac/banner-sach/11.png",
                "https://images.spiderum.com/sp-images/5fb105b035ab11ed8842ab3f8168fa40.png",
                "https://static.ladipage.net/628af78a4f2d59002016d3b6/ladi-1-20240408044010-mmbhj.png"
        };

        List<String> bannerList = Arrays.asList(banners);
        BannerAdapter adapter = new BannerAdapter(getContext(), bannerList);
        binding.viewPager2.setAdapter(adapter);

        new Handler().postDelayed(new Runnable() {

            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == bannerList.size()) currentPage = 0;
                binding.viewPager2.setCurrentItem(currentPage++, true);
                new Handler().postDelayed(this, 3000);
            }
        }, 3000);

        binding.nestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY + 10) {
                    // Cuộn xuống
                    binding.viewPager2.setVisibility(View.GONE);
                } else if (scrollY < oldScrollY - 10) {
                    // Cuộn lên
                    binding.viewPager2.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.imageViewFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFind = binding.edtFind.getText().toString();
                mViewModel.getBookListByTitle(strFind);

                fullBookList = mViewModel.getBookList().getValue();

                setBookAdapter();
            }
        });


    }

    public void setBookAdapter(){
        bookAdapter = new BookAdapter(fullBookList, getContext());
        binding.recycleViewHome.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recycleViewHome.setAdapter(bookAdapter);
    }
}