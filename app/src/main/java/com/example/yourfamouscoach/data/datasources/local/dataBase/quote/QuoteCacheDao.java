package com.example.yourfamouscoach.data.datasources.local.dataBase.quote;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface QuoteCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable saveQuotes(List<QuoteCache> quoteCache);

    @Query("DELETE FROM quote_cache")
    Completable clearCacheQuotes();

    @Query("SELECT * FROM quote_cache")
    Single<List<QuoteCache>> getQuotes();

    @Query("SELECT author FROM quote_cache WHERE quote = :quoteToSearch")
    Single<String> getAuthor(String quoteToSearch);
}
