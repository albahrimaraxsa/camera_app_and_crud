package com.example.lenovo.login.remote;

import com.example.lenovo.login.model.ResObj;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Lenovo on 16/08/2018.
 */

public interface UserService {
//    menggunakan GET
//    @GET("login.php/{username}/{password}")
//    Call<ResObj> login(@Path("username") String username, @Path("password") String password);
    @POST("login.php")
    @FormUrlEncoded
    Call<ResObj> login(
        @Field("username") String username,
        @Field("password") String password
    );
}
