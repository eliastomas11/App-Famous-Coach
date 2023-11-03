package com.example.yourfamouscoach.data.datasources.local.cache;

import android.content.Context;

import androidx.annotation.NonNull;

import androidx.datastore.rxjava3.RxDataStore;
import androidx.work.WorkerParameters;
import androidx.work.rxjava3.RxWorker;

import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteDatabase;
import com.example.yourfamouscoach.data.datasources.local.dataBase.quote.QuoteLocal;
import com.example.yourfamouscoach.data.datasources.preferences.RxDataStoreManager;
import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiClient;
import com.example.yourfamouscoach.data.datasources.remote.quotesource.service.ApiService;
import com.example.yourfamouscoach.data.mapper.QuoteDataMapper;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CacheUpdateWorker extends RxWorker {

    private final QuoteDatabase quoteLocal;
    private final QuoteCaching quoteCaching;
    private final ApiService apiService;
    private final RxDataStoreManager dataStoreManager;

    public CacheUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        quoteLocal = QuoteLocal.getDb(context);
        quoteCaching = new QuoteCaching(quoteLocal.getQuoteCacheDao(), new QuoteDataMapper());
        apiService = ApiClient.getApiClientInstance();
        dataStoreManager = RxDataStoreManager.getInstance(context);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        return apiService.getQuotes().flatMapCompletable(quoteCaching::saveQuotes)
                .doOnComplete(dataStoreManager::saveTimeStampCacheRefresh)
                .toSingleDefault(Result.success())
                .onErrorReturnItem(Result.failure());
    }


}
