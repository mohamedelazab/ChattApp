package com.example.mohamed.chattapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.api.ApiInterface;
import com.example.mohamed.chattapp.model.ChatRoom;
import com.example.mohamed.chattapp.model.MainResponse;

import retrofit2.Call;

public class DialogAddRoom extends Dialog {

    private EditText etRoomTitle, etRoomDesc;
    private Button btnAddRoom;
    private ApiInterface apiInterface;
    private  Call<MainResponse> call;
    private Context context;
    private ChatRoom newChatRoom;
    private AddRoomInterface addRoomInterface;

    public DialogAddRoom(@NonNull Context context, AddRoomInterface addRoomInterface) {
        super(context);
        this.context =context;
        this.addRoomInterface =addRoomInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_add_room);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        etRoomTitle =findViewById(R.id.et_chat_room_name);
        etRoomDesc =findViewById(R.id.et_chat_room_desc);
        btnAddRoom =findViewById(R.id.btn_add_room);
        newChatRoom =new ChatRoom();

        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRoomTitle.getText().toString().trim().equals("")){
                    etRoomTitle.setError("Required.!");
                }
                else if (etRoomDesc.getText().toString().trim().equals("")){
                    etRoomDesc.setError("Required.!");
                }
                else {
                    newChatRoom.setRoomName(etRoomTitle.getText().toString().trim());
                    newChatRoom.setRoomDescription(etRoomDesc.getText().toString().trim());
                    addRoomInterface.addNewChatRoomCallback(newChatRoom);
                }
            }
        });
    }

    public interface AddRoomInterface{
        void addNewChatRoomCallback(ChatRoom chatRoom);
    }
}