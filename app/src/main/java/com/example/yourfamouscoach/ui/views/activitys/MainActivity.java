package com.example.yourfamouscoach.ui.views.activitys;

import static com.example.yourfamouscoach.R.*;
import static com.example.yourfamouscoach.utils.AppConstants.ARG_AUTHOR_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.ARG_QUOTE_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.AUTHOR_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.QUOTE_KEY;
import static com.example.yourfamouscoach.utils.AppConstants.QUOTE_SAVED_KEY;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.ActivityMainBinding;
import com.example.yourfamouscoach.ui.interfaces.IFavoritesQuoteListView;
import com.example.yourfamouscoach.ui.interfaces.IMainPresenter;
import com.example.yourfamouscoach.ui.interfaces.IMainView;
import com.example.yourfamouscoach.ui.presenters.MainPresenter;
import com.example.yourfamouscoach.ui.views.navigation.NavigationCallback;
import com.example.yourfamouscoach.ui.views.navigation.NavigationImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMainView, NavigationCallback, HomeListener {

    private ActivityMainBinding binding;
    private IMainPresenter presenter;
    private List<FloatingActionButton> btnList;

    private Drawable randomFb;

    private FloatingActionButton lastClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        presenter = new MainPresenter(this);
        presenter.onUIstart();
        setContentView(binding.getRoot());
        NavigationImpl nav = new NavigationImpl(getSupportFragmentManager(), this);
        splashScreen.setKeepOnScreenCondition(() -> presenter.isLoading());

    }


    @Override
    public void clearUIConfig() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void hideEmotionsBtn() {
        btnList.forEach(btn -> {
            if(btn.getVisibility() == View.VISIBLE) {
            btn.startAnimation(AnimationUtils.loadAnimation(this, anim.to_bottom));
            btn.setVisibility(View.INVISIBLE);
            btn.setClickable(false);

            }
        });
    }


    @Override
    public void showEmotionsBtn() {
        btnList.forEach(btn -> {
            btn.setVisibility(View.VISIBLE);
            btn.setClickable(true);
            btn.startAnimation(AnimationUtils.loadAnimation(this, anim.from_bottom));
        });
    }

    @Override
    public void setUIEvents() {
        randomFb = binding.fbEmotions.getDrawable();
        lastClicked = null;
        binding.fbEmotions.setOnClickListener(v -> presenter.onFabClicked());
        btnList.forEach(btn -> btn.setOnClickListener(view -> changeEmoji((FloatingActionButton) view)));
    }

    private void changeEmoji(FloatingActionButton v) {
        Drawable temp = v.getDrawable();
        updateEmotion(v);
        if (lastClicked != null && lastClicked.getDrawable() == randomFb) {
            v.setImageDrawable(lastClicked.getDrawable());
            lastClicked.setImageDrawable(binding.fbEmotions.getDrawable());
        } else {
            v.setImageDrawable(binding.fbEmotions.getDrawable());
        }
        binding.fbEmotions.setImageDrawable(temp);
        lastClicked = v;
        presenter.onFabClicked();
    }

    private void updateEmotion(FloatingActionButton v){
        Log.d("ELIAS","UPDATE");
        if(v.getDrawable() == randomFb){
            Log.d("ELIAS","RANDOM");
            presenter.onEmotionUpdate("random");
        }
        else if (v.equals(binding.fbHappy)) {
            Log.d("ELIAS", "HAPPY");
            presenter.onEmotionUpdate("happy");
        } else if (v.equals(binding.fbAngry)) {
            presenter.onEmotionUpdate("angry");
            Log.d("ELIAS", "ANGRY");
        } else if (v.equals(binding.fbSad)) {
            presenter.onEmotionUpdate("sad");
            Log.d("ELIAS", "SAD");
        } else if (v.equals(binding.fbExcited)) {
            presenter.onEmotionUpdate("excited");
            Log.d("ELIAS", "EXCITED");
        } else if (v.equals(binding.fbWorried)) {
            presenter.onEmotionUpdate("worried");
            Log.d("ELIAS", "WORRIED");
        }

    }
    @Override
    public void loadResources() {
        btnList = Arrays.asList(binding.fbHappy, binding.fbAngry, binding.fbSad, binding.fbExcited, binding.fbWorried);
    }

    @Override
    public void hideBtmNavBar() {
        binding.btmAppBar.performHide();
    }

    @Override
    public void showBtmNavBar() {
        binding.btmAppBar.performShow();
    }

    @Override
    public void hideFbButton() {
        binding.fbEmotions.hide();
    }

    @Override
    public void showFbButton() {
        binding.fbEmotions.show();
        binding.fbEmotions.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ELIAS", "DESTROY");
        presenter = null;
        binding = null;
    }


    @Override
    public void onNavigateToSettings() {
        presenter.onSettingsNav();
    }

    @Override
    public void onNavigateToSavedQuotes() {
        presenter.onSavedNav();
    }

    @Override
    public void onNavigateToHome() {
        presenter.onHomeNav();
    }

    @Override
    public void setUpNavigation(NavController navController) {
        NavigationUI.setupWithNavController(binding.btmNav, navController);
    }

    @Override
    public Bundle onNavigationCreate() {
        if (getIntent().getExtras() != null) {
            Bundle argsForFragment = new Bundle();
            String quoteExtra = getIntent().getExtras().getString(QUOTE_KEY, "");
            String authorExtra = getIntent().getExtras().getString(AUTHOR_KEY, "");
            argsForFragment.putString(ARG_QUOTE_KEY, quoteExtra);
            argsForFragment.putString(ARG_AUTHOR_KEY, authorExtra);
            return argsForFragment;
        }
        return null;
    }

    @Override
    public String getActualEmotion() {
        return presenter.onEmotionRequest();
    }

    @Override
    public void dataIsLoaded(boolean loading) {
        presenter.onDataReceive(loading);
    }
}