package com.example.ndg.alocasiafarming.Rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://103.195.90.35:1600/skripsiapi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}