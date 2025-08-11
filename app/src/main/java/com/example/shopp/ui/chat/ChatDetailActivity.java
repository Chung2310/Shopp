package com.example.shopp.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.shopp.R;
import com.example.shopp.databinding.ActivityChatDetailBinding;
import com.example.shopp.model.Contact;
import com.example.shopp.model.User;
import com.example.shopp.ui.login.LoginViewModel;
import com.example.shopp.util.Utils;

public class ChatDetailActivity extends AppCompatActivity {
    private ActivityChatDetailBinding binding;
    private Contact contact;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        actionToolBar();

        contact = (Contact) getIntent().getSerializableExtra("contact");
        showDataUserContact(contact.getUserContactDTO());

        chatViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ChatViewModel.class);



        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.editTextMessage.getText() == null){
                    Toast.makeText(getApplicationContext(),"Tin nhắn không được để trống!",Toast.LENGTH_SHORT).show();
                }
                else {
                    String message = binding.editTextMessage.getText().toString();

                }
            }
        });
    }

    public void showDataUserContact(User user){
        binding.tvNameContactMessage.setText(user.getFullName());
        String newUrl = Utils.BASE_URL.replace("/api/", "/");

        String postAvatarUrl;

        if(contact.getUserContactDTO().getAvatarUrl() == null){
            Glide.with(getApplicationContext())
                    .load("")
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .error(R.drawable.ic_android_black_24dp)
                    .into(binding.circleImageMessager);
            return;
        }
        else {
            postAvatarUrl = contact.getUserContactDTO().getAvatarUrl().contains("https") ?
                    contact.getUserContactDTO().getAvatarUrl() : newUrl + "avatar/" + contact.getUserContactDTO().getAvatarUrl();
            Log.d("AvatarUrl",postAvatarUrl);
        }

        Glide.with(getApplicationContext())
                .load(postAvatarUrl)
                .placeholder(R.drawable.ic_android_black_24dp)
                .error(R.drawable.ic_android_black_24dp)
                .into(binding.circleImageMessager);

    }

    private void actionToolBar() {
        setSupportActionBar(binding.materialToolBarMessage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.materialToolBarMessage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}