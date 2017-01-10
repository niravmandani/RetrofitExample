package com.example.av004.retrofitexample;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by AV004 on 12/23/2016.
 */

public interface UsersAPI {
    @GET("/users")
   void getUsers(Callback<List<User>> response);


}
