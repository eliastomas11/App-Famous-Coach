package com.example.yourfamouscoach.domain.usecase.favoritequotes;

import java.util.List;

import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;
import io.reactivex.rxjava3.core.Single;

public class GetQuoteList {

    private IQuotesRepository quotesRepository;

    public GetQuoteList(IQuotesRepository quotesRepository) {
        this.quotesRepository = quotesRepository;
    }

    public Single<List<Quote>> getQuoteList(){
        return quotesRepository.getFavoritesQuotes();
    }

}
