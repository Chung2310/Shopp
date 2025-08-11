package com.example.shopp.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopp.R;
import com.example.shopp.databinding.FragmentChatBinding;
import com.example.shopp.repository.UserRepository;
import com.example.shopp.util.Utils;

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private FragmentChatBinding binding;
    private ChatAdapter chatAdapter;
    private UserRepository userRepository;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        userRepository = new UserRepository(getContext());

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        mViewModel.getContactByUserId((long) userRepository.getUser().getId());

        mViewModel.getMutableLiveDataContact().observe(getViewLifecycleOwner(), contacts -> {
            binding.recyclerView.setHasFixedSize(true);
            chatAdapter = new ChatAdapter(contacts, getContext());
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerView.setAdapter(chatAdapter);
        });

        mViewModel.getMsg().observe(getViewLifecycleOwner(), msg ->{

        });
    }

}