package com.example.yourfamouscoach.data.datasources.remote;

import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiService;

import java.util.List;

import com.example.yourfamouscoach.data.model.QuoteDto;
import io.reactivex.rxjava3.core.Single;

public class QuoteRemote {

    private final ApiService apiService;


    public QuoteRemote(ApiService apiService){
        this.apiService = apiService;
    }
    public Single<List<QuoteDto>> getRemoteQuotes(){
        return apiService.getQuotes();
    }

    public Single<List<QuoteDto>> getSingleQuote(){ return apiService.getQuoteForNotification();}

}
