package com.example.mohamed.chattapp.model;

import com.google.gson.annotations.SerializedName;

public class ChatRoom {

    @SerializedName("id")
    private int roomId;

    @SerializedName("room_name")
    private String roomName;

    @SerializedName("room_desc")
    private String roomDescription;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }
}
