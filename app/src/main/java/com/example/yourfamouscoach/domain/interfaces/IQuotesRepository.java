package com.example.yourfamouscoach.domain.interfaces;

import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.domain.model.Quote;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface IQuotesRepository {
    Single<List<Quote>> getQuotes();

    Completable saveQuote(Quote quote);

    Single<Quote> getQuoteWithEmotion(String emotion);

    Single<List<Quote>> getFavoritesQuotes();

    Completable deleteQuote(Quote quote);

    Single<Boolean> checkSaved(Quote quote);

    Single<String> getAuthor(String quote);
}
