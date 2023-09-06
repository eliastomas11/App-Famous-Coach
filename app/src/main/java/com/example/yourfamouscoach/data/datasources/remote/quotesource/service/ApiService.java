package com.example.yourfamouscoach.data.datasources.remote.quotesource.service;

import java.util.List;

import com.example.yourfamouscoach.data.model.QuoteDto;
import com.google.gson.annotations.Expose;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("quotes")
    Single<List<QuoteDto>> getQuotes();

    @GET("random")
    Call<List<QuoteDto>> getQuoteForNotification();
}
