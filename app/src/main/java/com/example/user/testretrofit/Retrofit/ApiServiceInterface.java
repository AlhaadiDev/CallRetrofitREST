package com.example.user.testretrofit.Retrofit;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;

public interface ApiServiceInterface {

    @POST("/oauth/token")
    Call<JsonObject> postAccessToken(@Body HashMap<String, String> inputParams);

    @POST("/auth/login")
    Call<JsonObject> UserLogin(@Body HashMap<String, String> inputParams);

    @GET("users/{id}")
    Call<JsonObject> userDetails(@Header("Authorization") String header, @Path("id") String id);
}
