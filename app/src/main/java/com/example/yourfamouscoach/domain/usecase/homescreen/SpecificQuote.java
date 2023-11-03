package com.example.yourfamouscoach.domain.usecase.homescreen;

import android.util.Log;

import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class SpecificQuote {
    private final IQuotesRepository quotesRepository;

    public SpecificQuote(IQuotesRepository quoteRepository) {
        this.quotesRepository = quoteRepository;
    }

    public Single<Quote> getAnswer(String emotion){
        return quotesRepository.getQuoteWithEmotion(emotion);
    }


}
