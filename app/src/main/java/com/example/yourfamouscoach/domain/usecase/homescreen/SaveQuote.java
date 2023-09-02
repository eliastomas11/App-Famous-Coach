package com.example.yourfamouscoach.domain.usecase.homescreen;

import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;

public class SaveQuote {

    private final IQuotesRepository quotesRepository;

    public SaveQuote(IQuotesRepository quoteRepository) {
        this.quotesRepository = quoteRepository;
    }

    public @NonNull Completable saveQuote(Quote quote) {

        return quotesRepository.saveQuote(quote);
    }

}
