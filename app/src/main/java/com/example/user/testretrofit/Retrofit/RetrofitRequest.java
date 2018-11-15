package com.example.user.testretrofit.Retrofit;

import com.example.user.testretrofit.Utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    private static Retrofit retrofit;
    private static Retrofit retrofit2;

    public static Retrofit getRetrofitRequest() {
        if (retrofit2 == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.interceptors().add(interceptor);
            OkHttpClient client = builder.build();
            retrofit2= new Retrofit.Builder()
                    .baseUrl(Utils.SERVER)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }

    public static Retrofit getRetrofitToken() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.SERVER_DOMAIN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
