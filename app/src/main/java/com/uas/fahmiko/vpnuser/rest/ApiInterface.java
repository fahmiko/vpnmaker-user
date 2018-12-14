package com.uas.fahmiko.vpnuser.rest;
import com.uas.fahmiko.vpnuser.model.*;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @GET("server/all")
    Call<GetServer> getServer();

    @FormUrlEncoded
    @POST("user/login")
    Call<GetUser> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @Multipart
    @POST("user/all")
    Call<GetUser> putUser(
            @Part MultipartBody.Part file,
            @Part("id_user") RequestBody id_user,
            @Part("name") RequestBody name,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("action") RequestBody action
    );
}
