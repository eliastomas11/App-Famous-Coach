package com.example.yourfamouscoach.ui.interfaces;

public interface IHomeView {

    void showQuote(String quoteText, String quoteAuthor);
    void showProgressBar();
    void hideProgressBar();

    void adaptText();

    void initViews();
    void showTextAnimations();

    void showBuddha();

    void showFavSaved();
    void showFavUnsaved();

    void showMessage(String message);

    void hideQuoteAndAuthorText();

    void launchPermissions();

    void shareQuote();
}
