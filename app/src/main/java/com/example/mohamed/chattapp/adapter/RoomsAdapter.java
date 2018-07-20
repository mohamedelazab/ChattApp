package com.example.mohamed.chattapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.model.ChatRoom;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder> {

    private List<ChatRoom> rooms;
    private Context context;
    private RoomCallback roomCallback;

    public RoomsAdapter(Context context, List<ChatRoom> rooms, RoomCallback roomCallback){
        this.context =context;
        this.rooms =rooms;
        this.roomCallback =roomCallback;
    }

    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.rv_room_item, parent, false);
        return new RoomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsViewHolder holder, int position) {
        holder.roomTitle.setText(rooms.get(position).getRoomName());
        holder.roomDesc.setText(rooms.get(position).getRoomDescription());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class RoomsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView roomImg;
        private TextView roomTitle, roomDesc;
        public RoomsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            roomImg =itemView.findViewById(R.id.room_img);
            roomTitle =itemView.findViewById(R.id.tv_room_title);
            roomDesc =itemView.findViewById(R.id.tv_room_desc);
        }

        @Override
        public void onClick(View view) {
            roomCallback.onRoomClick(rooms.get(getAdapterPosition()));
        }
    }

    public interface RoomCallback{
        void onRoomClick(ChatRoom room);
    }
}