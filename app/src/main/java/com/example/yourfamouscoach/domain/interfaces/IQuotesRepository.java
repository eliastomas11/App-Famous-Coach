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

    @NonNull Completable saveQuote(Quote quote);

    Single<Quote> getQuoteWithEmotion(String emotion);
    void delete();

    Single<List<Quote>> getFavoritesQuotes();

    Call<List<QuoteDto>> getSingleQuote();
}
