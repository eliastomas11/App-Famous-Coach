package com.example.yourfamouscoach.ui.interfaces;

public interface IHomeView {

    void showQuote(String quoteText, String quoteAuthor);
    void showProgressBar();
    void hideProgressBar();

    void adaptText();

    void initViews();
    void showTextAnimations();

    void showAuthorImageAnimation();

    void showFavSaved();
    void showFavUnsaved();

    void showMessage(String message);

    void hideQuoteAndAuthorText();

    void launchPermissions();

    void shareQuote();

    void checkSavedState(Boolean savedState);

    void showAuthorImage(String url,String quote,String author);

    void hideLoadingScreen();

    void showErrorAuthorImage();
}
