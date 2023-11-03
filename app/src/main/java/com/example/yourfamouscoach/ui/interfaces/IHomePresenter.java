package com.example.yourfamouscoach.ui.interfaces;

public interface IHomePresenter {

    void fetchData();

    void fetchSpecificQuote(String emotion);

    void onInitView();


    void onFavClicked(boolean saved,String s, String toString, String currentItem);

    void onShareClicked();

    void onImageLoad(String author,String quote);

    void onErrorImageLoad(String author, String quote);

    void onNotificationQuote(String quote, String author);

    boolean isLoading();

    void onDestroy();

    void onRestore(String quote,String author,Boolean isSaved);
}
