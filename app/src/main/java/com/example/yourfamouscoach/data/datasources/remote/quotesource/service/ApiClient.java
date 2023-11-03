package com.example.yourfamouscoach.data.datasources.remote.quotesource.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static volatile ApiService instance;
    private static final String url = "https://zenquotes.io/api/";

    private ApiClient() {
    }

    public static synchronized ApiService getApiClientInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder().
                    baseUrl(url).
                    addConverterFactory(GsonConverterFactory.create()).
                    addCallAdapterFactory(RxJava3CallAdapterFactory.create()).
                    build().create(ApiService.class);
        }
        return instance;
    }
}
