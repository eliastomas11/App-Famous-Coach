package com.example.yourfamouscoach.domain.usecase.homescreen;

import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;

import java.util.List;


import io.reactivex.rxjava3.core.Single;

public class GetQuotes {

    private final IQuotesRepository quotesRepository;

    public GetQuotes(IQuotesRepository quoteRepository) {
        this.quotesRepository = quoteRepository;
    }

    public Single<List<Quote>> getQuotes() {
        return quotesRepository.getQuotes();
    }


}
