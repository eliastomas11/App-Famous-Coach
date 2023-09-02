package com.example.yourfamouscoach.domain.model;

public class Quote {

    private String quote;
    private String author;
    private String emotion;


    public Quote(String quote, String author,String emotion) {
        this.quote = quote;
        this.author = author;
        this.emotion = emotion;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
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
