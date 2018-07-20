package com.example.mohamed.chattapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class User extends RealmObject{

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("is_user_admin")
    private int isUserAdmin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsUserAdmin(){
        return isUserAdmin;
    }

    public void setIsUserAdmin(int isUserAdmin){
        this.isUserAdmin =isUserAdmin;
    }
}