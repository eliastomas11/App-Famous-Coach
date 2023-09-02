package com.example.yourfamouscoach.data.datasources.local.dataBase.quote;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.yourfamouscoach.data.datasources.local.dataBase.relations.QuoteWithEmotion;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface QuoteDao {

    @Query("SELECT * FROM quote")
    List<QuoteEntity> getAll();

    @Query("INSERT INTO quote(quote,author,emotionId) VALUES (:quote,:author, (SELECT id FROM emotion WHERE name = :emotion))")
    void saveQuotes(String quote, String author, String emotion);

    @Query("DELETE FROM quote")
    void deleteQuotes();

    @Query("SELECT author FROM quote WHERE quote = :quoteToLook")
    String getAuthor(String quoteToLook);


//    @Query("SELECT * FROM quote, emotion WHERE quote.emotionId = emotion.id")
//    Single<List<QuoteWithEmotion>> getQuoteWithEmotion(); Funciona tambien

    @Query("SELECT * FROM quote")
    Single<List<QuoteWithEmotion>> getQuoteWithEmotion();


}
