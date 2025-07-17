package com.example.shopp.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel mViewModel;
    private RecyclerView recyclerViewHome;
    private BookAdapter bookAdapter;

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
        mViewModel.getBookList().observe(getViewLifecycleOwner(), books -> {
            binding.recycleViewHome.setHasFixedSize(true);

            bookAdapter = new BookAdapter(books, getContext());
            binding.recycleViewHome.setLayoutManager(new GridLayoutManager(getContext(),2));
            binding.recycleViewHome.setAdapter(bookAdapter);

        });
    }

}