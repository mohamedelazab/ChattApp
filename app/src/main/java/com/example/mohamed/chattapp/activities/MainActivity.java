package com.example.mohamed.chattapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.adapter.RoomsAdapter;
import com.example.mohamed.chattapp.api.ApiClient;
import com.example.mohamed.chattapp.api.ApiInterface;
import com.example.mohamed.chattapp.dialog.DialogAddRoom;
import com.example.mohamed.chattapp.model.ChatRoom;
import com.example.mohamed.chattapp.model.MainResponse;
import com.example.mohamed.chattapp.utils.Session;
import com.example.mohamed.chattapp.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RoomsAdapter.RoomCallback{

    Toolbar toolbar;
    TextView tvToolbarTitle;
    Button btnToolbarSignOut;

    RecyclerView rvChatRooms;
    RoomsAdapter roomsAdapter;
    LinearLayoutManager layoutManager;
    List<ChatRoom> rooms;

    ApiInterface apiInterface;
    Call<List<ChatRoom>> callGetRooms;
    Call<MainResponse> callAddRoom, callDeleteRoom;

    FloatingActionButton fabAddRoom;
    Animation fabClockWise, fabAntiClockWise;
    boolean isOpen;
    DialogAddRoom dialogAddRoom;
    ProgressBar progressBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnToolbarSignOut =findViewById(R.id.btn_toolbar_sign_out);
        tvToolbarTitle =findViewById(R.id.tv_toolbar_title);
        rvChatRooms =findViewById(R.id.rv_chat_rooms);
        progressBar =findViewById(R.id.progress_bar_main);
        rvChatRooms =findViewById(R.id.rv_chat_rooms);
        rooms =new ArrayList<>();
        layoutManager =new LinearLayoutManager(this);
        rvChatRooms.setLayoutManager(layoutManager);
        roomsAdapter =new RoomsAdapter(this,rooms,this);
        rvChatRooms.setAdapter(roomsAdapter);

        apiInterface = ApiClient.getApiClient(this).create(ApiInterface.class);


        User user = Session.getInstance().getUser();
        tvToolbarTitle.setText("Welcome " + user.getUsername());
        if (user.getIsUserAdmin() ==1){

            // delete feature available
            ItemTouchHelper itemTouchHelper =new ItemTouchHelper(swipeChatRoomCallback);
            itemTouchHelper.attachToRecyclerView(rvChatRooms);

            fabAddRoom =findViewById(R.id.fab_add_room);
            fabAddRoom.setVisibility(View.VISIBLE);
            fabClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise);
            fabAntiClockWise = AnimationUtils.loadAnimation(this, R.anim.rotate_anitclockwise);

            fabAddRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOpen) {
                        fabAddRoom.startAnimation(fabAntiClockWise);
                        isOpen = false;
                    } else {
                        fabAddRoom.startAnimation(fabClockWise);
                        isOpen = true;
                        dialogAddRoom = new DialogAddRoom(MainActivity.this, new DialogAddRoom.AddRoomInterface() {
                            @Override
                            public void addNewChatRoomCallback(final ChatRoom chatRoom) {
                                progressBar.setVisibility(View.VISIBLE);
                                callAddRoom =apiInterface.addChatRoom(chatRoom);
                                callAddRoom.enqueue(new Callback<MainResponse>() {
                                    @Override
                                    public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                                        Log.e("Chat_Room", chatRoom.getRoomName()+ chatRoom.getRoomDescription());
                                        Log.e("ADD_ROOM_RESPONSE", response.body().getStatus()+"\n"+response.body().getMessage());
                                        if (response.body().getStatus() ==1){
                                            Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            dialogAddRoom.dismiss();
                                            rooms.add(chatRoom);
                                            roomsAdapter.notifyDataSetChanged();
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            dialogAddRoom.dismiss();

                                        }

                                        fabAddRoom.startAnimation(fabAntiClockWise);
                                        isOpen = false;
                                    }

                                    @Override
                                    public void onFailure(Call<MainResponse> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, "Something Error, Try later.!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        dialogAddRoom.dismiss();
                                        fabAddRoom.startAnimation(fabAntiClockWise);
                                        isOpen = false;
                                    }
                                });

                            }
                        });

                        dialogAddRoom.show();
                    }
                }
            });

        }

        btnToolbarSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Session.getInstance().logoutUser();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        getRooms();
    }

    @Override
    public void onRoomClick(ChatRoom room) {
        //start chatActivity
        Intent intent =new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra("ROOM_ID", room.getRoomId());
        startActivity(intent);
    }

    private void getRooms(){
        progressBar.setVisibility(View.VISIBLE);
        callGetRooms =apiInterface.getAllChatRooms();
        callGetRooms.enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                rooms.clear();
                rooms.addAll(response.body());
                roomsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                Log.e("ROOMS_SIZE", rooms.size()+"");
            }
            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("ERROR", call.toString() + "\n Error Message:" +t.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        if (callAddRoom!=null){
            callAddRoom.cancel();
        }
        if (callGetRooms !=null) {
            callGetRooms.cancel();
        }
        if (callDeleteRoom !=null){
            callDeleteRoom.cancel();
        }
        super.onStop();
    }

    ItemTouchHelper.SimpleCallback swipeChatRoomCallback =new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position =viewHolder.getAdapterPosition();
            int chatRoomId =rooms.get(position).getRoomId();
            Log.e("chatRoomId", String.valueOf(chatRoomId));
            callDeleteRoom =apiInterface.deleteChatRoom(chatRoomId);
            callDeleteRoom.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (response.body().getStatus() ==1) {
                        Log.e("delete_response", response.body().getStatus()+"\n"+response.body().getMessage());
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        rooms.remove(position);
                        roomsAdapter.notifyItemRemoved(position);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState ==ItemTouchHelper.ACTION_STATE_SWIPE){
                View itemView =viewHolder.itemView;
                Paint paint =new Paint();
                if (dX <= 0){
                    paint.setColor(getResources().getColor(R.color.colorPrimary));
                    c.drawRect((float) itemView.getRight() +dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(),paint);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };
}