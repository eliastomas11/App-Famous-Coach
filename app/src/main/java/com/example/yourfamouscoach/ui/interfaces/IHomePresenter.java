package com.example.yourfamouscoach.ui.interfaces;

import com.example.yourfamouscoach.ui.model.QuotePresentation;

public interface IHomePresenter {

    void fetchData(boolean needsToShowQuote);

    void fetchSpecificQuote(String emotion);

    void onInitView();


    void onFavClicked(boolean saved,String s, String toString, String currentItem);

    void onShareClicked();

    void onNotificationQuote(String quote,String author);

    void onImageLoad(String author,String quote);
}
