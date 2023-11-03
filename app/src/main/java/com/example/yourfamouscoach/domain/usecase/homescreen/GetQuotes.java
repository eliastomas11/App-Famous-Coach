package com.example.yourfamouscoach.domain.usecase.homescreen;

import android.util.Log;

import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;


import io.reactivex.rxjava3.core.Single;

public class GetQuotes {

    private final IQuotesRepository quotesRepository;

    public GetQuotes(IQuotesRepository quoteRepository) {
        this.quotesRepository = quoteRepository;
    }

    public Single<Quote> getQuotes() {
        return quotesRepository.getQuotes().flatMap(quotes -> {
            if (quotes != null && !quotes.isEmpty()) {
                return Single.just(quotes.get(0));
            } else {
                throw new NoSuchElementException("The list is Empty or Null");
            }
        });
    }


}
