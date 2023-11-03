package com.example.yourfamouscoach.ui.interfaces;

public interface IMainPresenter {

    void onUIstart();

    void onFabClicked();

    void onEmotionUpdate(String emotion);

    void onSettingsNav();

    void onHomeNav();

    void onSavedNav();

    String onEmotionRequest();

    void onDataReceive(boolean loading);

    boolean isLoading();
}
