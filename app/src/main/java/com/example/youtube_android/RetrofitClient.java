package com.example.youtube_android;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(3, TimeUnit.SECONDS);
            httpClient.readTimeout(3, TimeUnit.SECONDS);
            httpClient.writeTimeout(3, TimeUnit.SECONDS);
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3001") // Use your server's base URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}

