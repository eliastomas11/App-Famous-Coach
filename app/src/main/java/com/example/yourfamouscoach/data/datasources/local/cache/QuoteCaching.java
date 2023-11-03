package com.example.yourfamouscoach.data.datasources.local.cache;

import android.util.Log;

import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteCacheDao;
import com.example.yourfamouscoach.data.mapper.QuoteDataMapper;
import com.example.yourfamouscoach.data.model.QuoteDto;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class QuoteCaching {

    private List<QuoteDto> quoteMemoryCache = Collections.emptyList();
    private QuoteCacheDao quoteDbCache;

    private QuoteDataMapper mapper;

    public QuoteCaching(QuoteCacheDao quoteDbCache, QuoteDataMapper mapper) {
        this.quoteDbCache = quoteDbCache;
        this.mapper = mapper;
    }

    public Single<List<QuoteDto>> getQuotes() {
        return quoteDbCache.getQuotes().flatMap(quoteCaches -> {
            if (!quoteCaches.isEmpty()) {
                Log.d("ELIAS","GETTING RANDOM FROM CACHE");
                return Single.just(mapper.cacheToDto(quoteCaches));
            } else {
                Log.d("ELIAS","GETTING RANDOM FROM MEMORY");

                return Single.just(quoteMemoryCache);
            }
        });
    }


    public Completable saveQuotes(List<QuoteDto> quoteList) {
        Log.d("ELIAS","SAVING TO CACHE");
        return clearCacheQuotes().andThen(quoteDbCache.saveQuotes(mapper.dtoToLocalCache(quoteList))
                .onErrorResumeNext(throwable -> saveToMemory(quoteList)));
    }


    public Single<String> getAuthor(String quoteToSearch) {
        return quoteDbCache.getAuthor(quoteToSearch);
    }

    public Completable saveToMemory(List<QuoteDto> quoteDtoList) {
        return Completable.fromAction(() -> quoteMemoryCache = quoteDtoList);
    }

    private Completable clearCacheQuotes() {
        Log.d("ELIAS","CLEANING CACHE");
        return quoteDbCache.clearCacheQuotes();
    }
}
