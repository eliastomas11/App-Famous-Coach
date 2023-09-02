package com.example.yourfamouscoach.data.datasources.local.dataBase.quote;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quote")
public class QuoteEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String quote;
    private String author;
    private int emotionId;

    public QuoteEntity() {
    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
