package com.example.yourfamouscoach.data.repository;


import android.util.Log;

import androidx.work.ListenableWorker;
import androidx.work.Worker;

import com.example.yourfamouscoach.data.datasources.local.dataBase.emotion.EmotionsDao;
import com.example.yourfamouscoach.data.datasources.remote.QuoteRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteDao;
import com.example.yourfamouscoach.data.datasources.local.ailocal.AiClient;
import com.example.yourfamouscoach.data.datasources.local.cache.QuoteCaching;
import com.example.yourfamouscoach.data.mapper.QuoteDataMapper;
import com.example.yourfamouscoach.data.model.Emotions;
import com.example.yourfamouscoach.data.model.QuoteDto;
import com.example.yourfamouscoach.domain.interfaces.IQuotesRepository;
import com.example.yourfamouscoach.domain.model.Quote;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public class QuoteRepositoryImpl implements IQuotesRepository {

    private final QuoteRemote quoteRemote;
    private final QuoteDao quoteLocal;
    private final AiClient aiClient;
    private final QuoteDataMapper mapper;
    private final QuoteCaching quoteCache;

    public QuoteRepositoryImpl(QuoteRemote quoteRemote, QuoteDao quoteLocal, AiClient aiClient, QuoteDataMapper mapper, QuoteCaching quoteCache) {
        this.quoteRemote = quoteRemote;
        this.quoteLocal = quoteLocal;
        this.aiClient = aiClient;
        this.mapper = mapper;
        this.quoteCache = quoteCache;
    }

    @Override
    public Single<List<Quote>> getQuotes() {
        return quoteCache.getQuotes().flatMap(quoteDtoList -> {
            if (quoteDtoList.isEmpty()) {
                Log.d("ELIAS","GETTING RANDOM FROM SERVER");
                return quoteRemote.getRemoteQuotes()
                        .flatMap(quoteRemoteResponse -> quoteCache.saveQuotes(quoteRemoteResponse)
                                .toSingleDefault(mapper.dtoToDomain(quoteRemoteResponse, Emotions.RANDOM.getName())));
            } else {
                Log.d("ELIAS","GETTING QUTOE FROM CACHE");
                return Single.just(mapper.dtoToDomain(quoteDtoList, Emotions.RANDOM.getName()));
            }});
    }


    @Override
    public @NonNull Completable saveQuote(Quote quote) {
        return Completable.fromAction(() -> quoteLocal.saveQuotes(quote.getQuote(), quote.getAuthor(), quote.getEmotion()));
    }

    @Override
    public Single<Quote> getQuoteWithEmotion(String emotion) {
        return quoteCache.getQuotes().flatMap(quoteDtoList -> {
            if (emotion.equalsIgnoreCase(Emotions.RANDOM.getName())) {
                Log.d("ELIAS","GETTING RANDOM FROM CACHE");
                return getRandomQuote(quoteDtoList, emotion);
            } else {
                return aiClient.getAnswer(emotion, quoteDtoList).flatMap(aiResponseModel ->
                        getAuthor(aiResponseModel.getGenerated_text()).flatMap(author ->
                                author.isEmpty() ? getRandomQuote(quoteDtoList, emotion) : Single.just(mapper.aiToDomain(aiResponseModel, author, emotion))));
            }
        });

    }

    private Single<Quote> getRandomQuote(List<QuoteDto> quoteDtos, String emotion) {
        return Single.just(mapper.transformDto(quoteDtos.get(new Random().nextInt(quoteDtos.size())), emotion));
    }

    public Single<String> getAuthor(String quote) {
        return quoteCache.getAuthor(quote)
                .map(authorName -> authorName != null ? authorName : "")
                .onErrorReturnItem("");
    }

    @Override
    public Single<List<Quote>> getFavoritesQuotes() {
        return quoteLocal.getQuoteWithEmotion().map(quoteWithEmotions -> {
            ArrayList<Quote> quotesList = new ArrayList<>();
            quoteWithEmotions.forEach(quoteWithEmotion -> quotesList.add(
                    mapper.transformEntity(quoteWithEmotion.quoteEntity, quoteWithEmotion.emotionEntity.getName().getName())));
            return quotesList;
        });
    }

    @Override
    public Completable deleteQuote(Quote quote) {
        return quoteLocal.deleteQuote(quote.getQuote());
    }

    @Override
    public Single<Boolean> checkSaved(Quote quote) {
        return quoteLocal.checkSaved(quote.getQuote()).map(integer -> integer > 0);
    }

}
