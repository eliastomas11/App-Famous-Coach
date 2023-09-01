package com.example.yourfamouscoach.ui.presenters;

import com.example.yourfamouscoach.ui.interfaces.IMainPresenter;
import com.example.yourfamouscoach.ui.interfaces.IMainView;

public class MainPresenter implements IMainPresenter {

    private final IMainView view;


    public MainPresenter(IMainView view) {
        this.view = view;
    }

    @Override
    public void onMenuClicked() {
        view.showDrawerMenu();
    }

    @Override
    public void onBackMenuPressed() {
        view.hideDrawerMenu();
    }


}
