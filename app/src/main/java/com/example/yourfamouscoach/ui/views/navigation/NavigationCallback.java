package com.example.yourfamouscoach.ui.views.navigation;

import android.os.Bundle;

import androidx.navigation.NavController;

public interface NavigationCallback {

    void onNavigateToSettings();
    void onNavigateToSavedQuotes();
    void onNavigateToHome();

    void setUpNavigation(NavController navController);

    Bundle onNavigationCreate();
}
