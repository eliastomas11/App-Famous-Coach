package com.example.yourfamouscoach.data.model;

import com.google.gson.annotations.SerializedName;

public class QuoteDto {

    @SerializedName("q")
    private String quote;
    @SerializedName("a")
    private String author;

    public QuoteDto(String quote, String author) {
        this.quote = quote;
        this.author = author;
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
