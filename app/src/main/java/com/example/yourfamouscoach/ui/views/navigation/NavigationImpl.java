package com.example.yourfamouscoach.ui.views.navigation;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.yourfamouscoach.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NavigationImpl {

    private final NavController controller;
    private final NavHostFragment navHost;
    private final NavigationCallback callback;

    public NavigationImpl(FragmentManager fragmentManager, NavigationCallback callback) {
        this.navHost = (NavHostFragment)fragmentManager.findFragmentById(R.id.fragmentContainer);
        this.controller = navHost.getNavController();
        this.callback = callback;
        setUpNavigation();
    }

    private void setUpNavigation(){
        controller.setGraph(R.navigation.main_graph,callback.onNavigationCreate());
        callback.setUpNavigation(controller);
        navigationListener();
    }

    public void navigationListener() {
        controller.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.settingsFragment) {
                callback.onNavigateToSettings();
            }
            if (navDestination.getId() == R.id.favoriteQuotesScreen){
                callback.onNavigateToSavedQuotes();
            }
            if(navDestination.getId() == R.id.homeScreen){
                callback.onNavigateToHome();
            }
        });
    }

}
