package domain.interfaces;

import java.util.List;
import java.util.Map;

import data.datasources.local.dataBase.emotion.EmotionEntity;
import data.datasources.local.dataBase.quote.QuoteEntity;
import domain.model.Quote;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface IQuotesRepository {
    Single<List<Quote>> getQuotes();

    @NonNull Completable saveQuote(Quote quote);

    Single<Quote> getQuoteWithEmotion(String emotion);
    void delete();

    Single<List<Quote>> getFavoritesQuotes();
}
