package com.example.mohamed.chattapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainResponse implements Serializable{

    @SerializedName("status")
    private
    int status;

    @SerializedName("message")
    private
    String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
