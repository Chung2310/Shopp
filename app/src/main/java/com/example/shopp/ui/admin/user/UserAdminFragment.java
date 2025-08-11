package com.example.shopp.ui.admin.user;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopp.R;
import com.example.shopp.databinding.FragmentUserAdminBinding;
import com.example.shopp.model.User;
import com.example.shopp.model.UserAdmin;

import java.util.ArrayList;
import java.util.List;

public class UserAdminFragment extends Fragment {

    private UserAdminViewModel mViewModel;
    private FragmentUserAdminBinding binding;
    private UserAdminAdapter userAdminAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserAdminViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getAllUserAdmin();

        mViewModel.getListMutableLiveData().observe(getViewLifecycleOwner() , userList ->{
            if(userList != null){
                setAdapter(userList);
            }

        });


    }

    private void setAdapter(List<UserAdmin> users) {
        userAdminAdapter = new UserAdminAdapter(users, getContext());
        binding.recyclerViewUserAmin.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewUserAmin.setAdapter(userAdminAdapter);
    }


}