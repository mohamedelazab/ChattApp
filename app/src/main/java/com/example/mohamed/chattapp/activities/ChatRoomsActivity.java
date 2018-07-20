package com.example.mohamed.chattapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.adapter.RoomsAdapter;
import com.example.mohamed.chattapp.api.ApiClient;
import com.example.mohamed.chattapp.api.ApiInterface;
import com.example.mohamed.chattapp.model.ChatRoom;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomsActivity extends AppCompatActivity implements RoomsAdapter.RoomCallback{

    RecyclerView rvChatRooms;
    RoomsAdapter roomsAdapter;
    LinearLayoutManager layoutManager;
    List<ChatRoom> rooms;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);
        rvChatRooms =findViewById(R.id.rv_chat_rooms);
        rooms =new ArrayList<>();
        roomsAdapter =new RoomsAdapter(this,rooms,this);
        layoutManager =new LinearLayoutManager(this);
        rvChatRooms.setLayoutManager(layoutManager);
        rvChatRooms.setAdapter(roomsAdapter);

        apiInterface =ApiClient.getApiClient(this).create(ApiInterface.class);
        Call<List<ChatRoom>> call =apiInterface.getAllChatRooms();
        call.enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                rooms =response.body();
                roomsAdapter.notifyDataSetChanged();
                Log.e("ROOMS_SIZE", rooms.size()+"");
            }
            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                Log.e("ERROR", call.toString() + "\n Error Message:" +t.getMessage());
            }
        });
    }

    @Override
    public void onRoomClick(ChatRoom room) {
        Toast.makeText(this, ""+room.getRoomName(), Toast.LENGTH_SHORT).show();
    }
}
