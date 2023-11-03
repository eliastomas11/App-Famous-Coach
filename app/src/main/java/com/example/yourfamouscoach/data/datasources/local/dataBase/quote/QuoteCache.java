package com.example.yourfamouscoach.data.datasources.local.dataBase.quote;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "quote_cache",indices = {@Index(value = {"quote"}, unique = true)})
public class QuoteCache {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String quote;

    private String author;

    public QuoteCache() {
    }

    public QuoteCache(String quote, String author) {
        this.quote = quote;
        this.author = author;
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
