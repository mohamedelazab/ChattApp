package com.example.mohamed.chattapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.adapter.MessagingAdapter;
import com.example.mohamed.chattapp.api.ApiClient;
import com.example.mohamed.chattapp.api.ApiInterface;
import com.example.mohamed.chattapp.model.MainResponse;
import com.example.mohamed.chattapp.model.Message;
import com.example.mohamed.chattapp.utils.Session;
import com.example.mohamed.chattapp.utils.Constants;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvToolbarTitle;
    Button btnToolbarSignOut;
    ImageButton btnSendMsg, btnSendImg;
    EditText etMessage;
    RecyclerView rvChat;
    LinearLayoutManager layoutManager;
    MessagingAdapter messagingAdapter;
    List<Message> messages;
    String username;
    int userId;

    BroadcastReceiver messagesReceiver;

    ApiInterface apiInterface;
    Call<List<Message>> callGetMessages;
    Call<MainResponse> callAddMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnToolbarSignOut =findViewById(R.id.btn_toolbar_sign_out);
        tvToolbarTitle =findViewById(R.id.tv_toolbar_title);
        username =Session.getInstance().getUser().getUsername();
        userId =Session.getInstance().getUser().getId();
        final int roomId =getIntent().getExtras().getInt("ROOM_ID");
        tvToolbarTitle.setText(username);
        messages =new ArrayList<>();
        rvChat =findViewById(R.id.rv_chat);
        messagingAdapter =new MessagingAdapter(this,messages);
        layoutManager =new LinearLayoutManager(this);
        rvChat.setLayoutManager(layoutManager);
        rvChat.setAdapter(messagingAdapter);

        btnSendMsg =findViewById(R.id.btn_send_msg);
        btnSendImg =findViewById(R.id.btn_send_img);
        etMessage =findViewById(R.id.et_message);

        FirebaseMessaging.getInstance().subscribeToTopic("room" +roomId);

        messagesReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message =intent.getParcelableExtra(Constants.MESSAGE_PASS_INTENT);
                if (message !=null){
                    messages.add(message);
                    messagingAdapter.notifyItemInserted(messages.size() -1);
                    rvChat.scrollToPosition(messages.size() -1);
                }
            }
        };

        apiInterface = ApiClient.getApiClient(this).create(ApiInterface.class);
        loadPreviousMessages(roomId);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageContent =etMessage.getText().toString().trim();
                if (!messageContent.equals("")) {
                    Message message = new Message();
                    message.setRoomId(String.valueOf(roomId));
                    message.setType("1");
                    message.setUserId(String.valueOf(userId));
                    message.setUserName(username);
                    message.setContent(messageContent);
                    messages.add(message);
                    messagingAdapter.notifyItemInserted(messages.size() - 1);
                    rvChat.scrollToPosition(messages.size() - 1);
                    etMessage.setText("");
                    addNewMessage(message);
                }
            }
        });

        btnSendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loadPreviousMessages(int roomId) {
        callGetMessages =apiInterface.getMessages(roomId);
        callGetMessages.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messages.clear();
                messages =response.body();
                messagingAdapter =null;
                messagingAdapter =new MessagingAdapter(ChatActivity.this, messages);
                rvChat.setAdapter(null);
                rvChat.setAdapter(messagingAdapter);
                Log.e("SIZE","size: "+messages.size() +" "+messages.get(0).getContent());
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.e("get_messages_error", t.getMessage());
                Toast.makeText(ChatActivity.this, "Can't load messages.!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewMessage(Message message){
        callAddMessage =apiInterface.addMessage(message);
        callAddMessage.enqueue(new Callback<MainResponse>() {
            @Override
            public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                if (response.body().getStatus() ==0){
                    Toast.makeText(ChatActivity.this, "Error While Sending Message.!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MainResponse> call, Throwable t) {
                Log.e("sent_messages_error", t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messagesReceiver, new IntentFilter(Constants.UPDATE_CHAT_ACTIVITY_BROADCAST));
    }

    @Override
    protected void onStop() {
        if (callGetMessages !=null){
            callGetMessages.cancel();
        }
        else if (callAddMessage !=null){
            callAddMessage.cancel();
        }
        super.onStop();
        unregisterReceiver(messagesReceiver);
    }
}