package com.example.yourfamouscoach.ui.interfaces;

public interface IHomePresenter {

    void fetchData(boolean needsToShowQuote);

    void fetchSpecificQuote(String emotion);

    void onInitView();


    void onFavClicked(String s, String toString, String currentItem);

    void onShareClicked();

    void onNotificationQuote(String quote,String author);
}
