package com.example.yourfamouscoach.ui.views.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.ActivityMainBinding;
import com.example.yourfamouscoach.ui.interfaces.IMainPresenter;
import com.example.yourfamouscoach.ui.interfaces.IMainView;
import com.example.yourfamouscoach.ui.presenters.MainPresenter;
import com.example.yourfamouscoach.ui.views.fragments.AboutScreen;
import com.example.yourfamouscoach.ui.views.fragments.FavoriteQuotesScreen;
import com.example.yourfamouscoach.ui.views.fragments.HomeScreen;
import com.example.yourfamouscoach.ui.views.fragments.SettingsScreen;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements IMainView {

    private ActivityMainBinding binding;
    private IMainPresenter presenter;
    //private AppContainer appContainer;
    private ActionBarDrawerToggle toggle;

    private int container;
    Bundle argsForFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //appContainer = ((MyApplication) getApplication()).appContainer;
        presenter = new MainPresenter(this);
        container = R.id.fragmentContainer;
        if(getIntent().getExtras() != null){
            String quoteExtra = getIntent().getExtras().getString("not","quote");
            String authorExtra = getIntent().getExtras().getString("aut","aut");
            argsForFragment = new Bundle();
            argsForFragment.putString("quote",quoteExtra);
            argsForFragment.putString("author",authorExtra);
        }
        if (savedInstanceState == null) {
            changeScreen(container, HomeScreen.class, argsForFragment);
        }
        initNavDrawerMenu();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            presenter.onBackMenuPressed();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initNavDrawerMenu() {
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.openNavDrawer, R.string.closeNavDrawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miAbout:
                        changeScreen(container, AboutScreen.class, argsForFragment);
                        break;
                    case R.id.miFavorites:
                        changeScreen(container, FavoriteQuotesScreen.class, argsForFragment);
                        break;
                    case R.id.miHome:
                        changeScreen(container, HomeScreen.class, argsForFragment);
                        ;
                        break;
                    case R.id.miSettings:
                        changeScreen(container, SettingsScreen.class, argsForFragment);
                        break;
                    case R.id.miRateUs:
                        break;
                    case R.id.miShareApp:
                        break;
                }
                return true;
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
        binding = null;
        //appContainer = null;
    }


    @Override
    public void showDrawerMenu() {

    }

    @Override
    public void hideDrawerMenu() {

    }

    private <T extends Fragment> void changeScreen(int screen, Class<T> tClass, Bundle args) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(screen, tClass, args)
                .addToBackStack(null)
                .commit();

    }

}