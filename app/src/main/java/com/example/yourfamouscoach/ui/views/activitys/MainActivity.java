package com.example.yourfamouscoach.ui.views.activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.ActivityMainBinding;
import com.example.yourfamouscoach.ui.interfaces.IMainPresenter;
import com.example.yourfamouscoach.ui.interfaces.IMainView;
import com.example.yourfamouscoach.ui.presenters.MainPresenter;
import com.example.yourfamouscoach.ui.views.fragments.AboutScreen;
import com.example.yourfamouscoach.ui.views.fragments.FavoriteQuotesScreen;
import com.example.yourfamouscoach.ui.views.fragments.HomeScreen;
import com.example.yourfamouscoach.ui.views.fragments.SettingsFragment;
import com.example.yourfamouscoach.ui.views.fragments.SettingsScreen;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IMainView {

    private ActivityMainBinding binding;
    private IMainPresenter presenter;

    private int container;
    Bundle argsForFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );

        setContentView(binding.getRoot());
        presenter = new MainPresenter(this);
        container = R.id.fragmentContainer;
        splashScreen.setKeepOnScreenCondition(() -> false);

        if (getIntent().getExtras() != null) {
            String quoteExtra = getIntent().getExtras().getString("not", "quote");
            String authorExtra = getIntent().getExtras().getString("aut", "aut");
            argsForFragment = new Bundle();
            argsForFragment.putString("quote", quoteExtra);
            argsForFragment.putString("author", authorExtra);
        }
        if (savedInstanceState == null) {
            changeScreen(container, HomeScreen.class, argsForFragment);
        }

        binding.btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.imHome:
                        changeScreen(R.id.fragmentContainer, HomeScreen.class, argsForFragment);
                        break;
                    case R.id.imFavorites:
                        changeScreen(R.id.fragmentContainer, FavoriteQuotesScreen.class, null);
                        break;
                    case R.id.imSettings:
                        changeScreen(R.id.fragmentContainer, SettingsFragment.class, null);
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


    private <T extends Fragment> void changeScreen(int screen, Class<T> tClass, Bundle args) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(screen, tClass, args)
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .addToBackStack(null)
                .commit();

    }


}