package com.example.mohamed.chattapp.api;

import com.example.mohamed.chattapp.model.ChatRoom;
import com.example.mohamed.chattapp.model.LoginResponse;
import com.example.mohamed.chattapp.model.MainResponse;
import com.example.mohamed.chattapp.model.Message;
import com.example.mohamed.chattapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("login_user.php")
    Call<LoginResponse> loginUser(@Body User user);

//    @Headers("Content-Type: application/json")
    @POST("register_user.php")
    Call<MainResponse> registerUser(@Body User user);

    @GET("get_all_chat_rooms.php")
    Call<List<ChatRoom>> getAllChatRooms();

    @POST("add_chat_rooms.php")
    Call<MainResponse> addChatRoom(@Body ChatRoom chatRoom);

    @FormUrlEncoded
    @POST("delete_chat_room.php")
    Call<MainResponse> deleteChatRoom(@Field("id") int roomId);

    @POST("add_message.php")
    Call<MainResponse> addMessage(@Body Message message);

    @FormUrlEncoded
    @POST("get_messages.php")
    Call<List<Message>> getMessages(@Field("room_id") int roomId);
}