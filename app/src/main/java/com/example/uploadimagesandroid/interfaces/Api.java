package com.example.uploadimagesandroid.interfaces;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    @Multipart
    @POST("img/updateavatar")
    Call<RequestBody> uploadImage(@Part MultipartBody.Part part);
}
