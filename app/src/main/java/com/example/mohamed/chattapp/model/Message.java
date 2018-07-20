package com.example.mohamed.chattapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Message implements Parcelable{

    @SerializedName("id")
    private String id;

    @SerializedName("room_id")
    private String roomId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("type")
    private String type;

    @SerializedName("content")
    private String content;

    @SerializedName("timestamp")
    private String timestamp;

    public Message(){

    }

    protected Message(Parcel in) {
        id = in.readString();
        roomId = in.readString();
        userId = in.readString();
        userName = in.readString();
        type = in.readString();
        content = in.readString();
        timestamp = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        if (timestamp == null) {
            return "now";
        } else {
            SimpleDateFormat dateFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date date = null;
            try {
                date = dateFormat.parse(timestamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TimeZone timeZone = TimeZone.getDefault();
            int rawOffset = timeZone.getRawOffset();
            long local = 0;
            if (date != null) {
                local = date.getTime() + rawOffset;
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(local);
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            return simpleDateFormat.format(calendar.getTime());
        }
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(roomId);
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(type);
        parcel.writeString(content);
        parcel.writeString(timestamp);
    }
}