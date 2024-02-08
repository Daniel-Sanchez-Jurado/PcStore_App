package com.example.pcstore.Retrofit;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestApi {
    String BASE_URL = "http://10.0.2.2:8080/";

    @GET("/articles")
    Call<ArrayList<Article>> getArticles();

    @POST("/articles")
    Call<Long> addArticle(@Body Article newArticle);

    @DELETE("/articles/{id}")
    Call<Response<ResponseBody>> deleteArticle(@Path("id") Long id);
}
