package com.example.yourfamouscoach.ui.model;

public class QuotePresentation {

    private String quote;
    private String author;

    private String emotion;

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

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public QuotePresentation(String quote, String author) {
        this.quote = quote;
        this.author = author;
    }

    public QuotePresentation(String quote, String author, String emotion) {
        this.quote = quote;
        this.author = author;
        this.emotion = emotion;
    }
}
