package com.example.yourfamouscoach.domain.usecase.favoritequotes;

import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;

import io.reactivex.rxjava3.core.Completable;

public class DeleteSavedQuote {

    public IQuotesRepository quotesRepository;

    public DeleteSavedQuote(IQuotesRepository quotesRepository){
        this.quotesRepository = quotesRepository;
    }

    public Completable deleteSavedQuote(Quote quote){
        return quotesRepository.deleteQuote(quote);
    }
}
