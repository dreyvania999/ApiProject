package com.example.apiproject;

import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.DELETE;
        import retrofit2.http.POST;
        import retrofit2.http.PUT;
        import retrofit2.http.Query;

public interface RetrofitAPI {

    @POST("toothpastes/")
    Call<Mask> createPost(@Body Mask mask);

    @PUT("toothpastes/{id}")
    Call<Mask> updateData(@Query("id") int id_toothpaste, @Body Mask mask);

    @DELETE("toothpastes/{id}")
    Call<Mask> deleteData(@Query("id") int id_toothpaste);

}