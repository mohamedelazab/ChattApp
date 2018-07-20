package com.example.mohamed.chattapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private LoginUser loginUser;

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public static class LoginUser{
        @SerializedName("id")
        private int id;

        @SerializedName("user_name")
        private String username;

        @SerializedName("user_email")
        private String email;

        @SerializedName("user_password")
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

        public int getIsUserAdmin() {
            return isUserAdmin;
        }

        public void setIsUserAdmin(int isUserAdmin) {
            this.isUserAdmin = isUserAdmin;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
