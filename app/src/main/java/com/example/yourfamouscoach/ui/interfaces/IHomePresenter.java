package com.example.yourfamouscoach.ui.interfaces;

public interface IHomePresenter {

    void fetchData();

    void fetchSpecificQuote(String emotion);

    void onInitView();


    void onFavClicked(String s, String toString, String currentItem);

    void onShareClicked();
}
