package com.example.yourfamouscoach.ui.presenters;


import com.example.yourfamouscoach.domain.usecase.homescreen.GetQuotes;
import com.example.yourfamouscoach.ui.interfaces.IMainPresenter;
import com.example.yourfamouscoach.ui.interfaces.IMainView;

import java.util.Arrays;
import java.util.List;


public class MainPresenter implements IMainPresenter {

    private final IMainView view;
    private boolean showState = false;
    private String actualEmotion = "random";
    private boolean loadingScreen = true;

    public MainPresenter(IMainView view) {
        this.view = view;
    }

    @Override
    public void onUIstart() {
        view.loadResources();
        view.setUIEvents();
    }

    @Override
    public void onFabClicked() {
        if (showState) {
            view.hideEmotionsBtn();
        } else {
            view.showEmotionsBtn();
        }
        showState = !showState;
    }

    @Override
    public void onEmotionUpdate(String emotion) {
        actualEmotion = emotion;
    }

    @Override
    public void onSettingsNav() {
        hideEmotions();
        view.hideBtmNavBar();
        view.hideFbButton();
        view.clearUIConfig();
    }

    @Override
    public void onHomeNav() {
        hideEmotions();
        view.showBtmNavBar();
        view.showFbButton();
    }

    @Override
    public void onSavedNav() {
        hideEmotions();
    }

    @Override
    public String onEmotionRequest() {
        return actualEmotion;
    }

    @Override
    public void onDataReceive(boolean loading) {
        loadingScreen = loading;
    }

    @Override
    public boolean isLoading() {
        return loadingScreen;
    }

    private void hideEmotions() {
        view.hideEmotionsBtn();
    }

}
