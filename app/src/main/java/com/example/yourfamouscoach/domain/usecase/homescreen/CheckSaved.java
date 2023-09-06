package com.example.yourfamouscoach.domain.usecase.homescreen;

import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;

import io.reactivex.rxjava3.core.Single;

public class CheckSaved {

    private IQuotesRepository quotesRepository;

    public CheckSaved(IQuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }

    public Single<Boolean> checkSaved(Quote quote){
        return quotesRepository.checkSaved(quote);
    }
}
