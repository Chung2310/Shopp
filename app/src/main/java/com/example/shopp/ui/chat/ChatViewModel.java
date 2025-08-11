package com.example.shopp.ui.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.shopp.model.ChatMessage;
import com.example.shopp.model.Contact;
import com.example.shopp.retrofit.Api;
import com.example.shopp.retrofit.RetrofitClient;
import com.example.shopp.util.Utils;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ua.naiksoftware.stomp.StompClient;

public class ChatViewModel extends AndroidViewModel {

    private final Api api;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<String> msg = new MutableLiveData<>();
    private final MutableLiveData<List<Contact>> mutableLiveDataContact = new MutableLiveData<>();
    private final MutableLiveData<ChatMessage> liveChatMessage = new MutableLiveData<>();

    public ChatViewModel(@NonNull Application application) {
        super(application);
        api = RetrofitClient.getInstance(Utils.BASE_URL, application).create(Api.class);
    }

    public LiveData<String> getMsg() {
        return msg;
    }

    public LiveData<List<Contact>> getMutableLiveDataContact() {
        return mutableLiveDataContact;
    }

    public LiveData<ChatMessage> getLiveChatMessage() {
        return liveChatMessage;
    }

    public void getContactByUserId(Long userId){
        compositeDisposable.add(api.getContactByUserId(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            if (result.getStatus() == 200) {
                                mutableLiveDataContact.setValue(result.getResult());
                            }
                            msg.setValue(result.getMessage());
                        },
                        throwable -> {
                            Log.e("ChatViewModel", "Lỗi getContactByUserId", throwable);
                            msg.setValue("Lỗi kết nối máy chủ");
                        }
                ));
    }

    public void createContact(Contact contact){
        compositeDisposable.add(api.createContact(contact)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> msg.setValue(result.getMessage()),
                        throwable -> {
                            Log.e("ChatViewModel", "Lỗi createContact", throwable);
                            msg.setValue("Không thể tạo liên hệ");
                        }
                ));
    }

    public void deleteContact(Long userId, Long userContactId){
        compositeDisposable.add(api.deleteContract(userId, userContactId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> msg.setValue(result.getMessage()),
                        throwable -> {
                            Log.e("ChatViewModel", "Lỗi deleteContact", throwable);
                            msg.setValue("Không thể xóa liên hệ");
                        }
                ));
    }

}
