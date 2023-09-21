package com.example.yourfamouscoach.ui.views.activitys;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.databinding.ActivityMainBinding;
import com.example.yourfamouscoach.ui.interfaces.IMainPresenter;
import com.example.yourfamouscoach.ui.interfaces.IMainView;
import com.example.yourfamouscoach.ui.presenters.MainPresenter;
import com.example.yourfamouscoach.ui.resources.Emojis;
import com.example.yourfamouscoach.ui.views.fragments.FavoriteQuotesScreen;
import com.example.yourfamouscoach.ui.views.fragments.HomeScreen;
import com.example.yourfamouscoach.ui.views.fragments.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements IMainView {

    private ActivityMainBinding binding;
    private IMainPresenter presenter;

    private int container;
    Bundle argsForFragment = null;

    private boolean clicked = false;
    private FloatingActionButton previosEmoji;
    private Emojis actualEmoji;
    private Animation fromBottom;
    private Animation toBottom;

    private Drawable random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(binding.getRoot());
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom);
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
        binding.btmAppBar.setHideOnScroll(true);
        actualEmoji = Emojis.RANDOM;
        random = binding.fbEmotions.getDrawable();
        previosEmoji = binding.fbEmotions;
        binding.fbEmotions.setOnClickListener(v -> {
            onFabEmotionClicked();
        });
        binding.fbHappy.setOnClickListener(v -> {
            changeEmoji((FloatingActionButton) v, previosEmoji);
            previosEmoji = (FloatingActionButton) v;
            actualEmoji = Emojis.HAPPY;
        });
        binding.fbAngry.setOnClickListener(v -> {
            changeEmoji((FloatingActionButton) v, previosEmoji);
            previosEmoji = (FloatingActionButton) v;
            actualEmoji = Emojis.ANGRY;
        });
        binding.fbWorried.setOnClickListener(v -> {
            changeEmoji((FloatingActionButton) v, previosEmoji);
            previosEmoji = (FloatingActionButton) v;
            actualEmoji = Emojis.WORRIED;
        });
        binding.fbExcited.setOnClickListener(v -> {
            changeEmoji((FloatingActionButton) v, previosEmoji);
            previosEmoji = (FloatingActionButton) v;
            actualEmoji = Emojis.EXCITED;
        });
        binding.fbSad.setOnClickListener(v -> {
            changeEmoji((FloatingActionButton) v, previosEmoji);
            previosEmoji = (FloatingActionButton) v;
            actualEmoji = Emojis.SAD;
        });
    }

    private void onFabEmotionClicked() {
        setVisibility(clicked);
        setClickable(clicked);
        showAnimations(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            binding.fbAngry.setVisibility(View.VISIBLE);
            binding.fbExcited.setVisibility(View.VISIBLE);
            binding.fbSad.setVisibility(View.VISIBLE);
            binding.fbHappy.setVisibility(View.VISIBLE);
            binding.fbWorried.setVisibility(View.VISIBLE);
        } else {
            binding.fbAngry.setVisibility(View.INVISIBLE);
            binding.fbExcited.setVisibility(View.INVISIBLE);
            binding.fbSad.setVisibility(View.INVISIBLE);
            binding.fbHappy.setVisibility(View.INVISIBLE);
            binding.fbWorried.setVisibility(View.INVISIBLE);
        }
    }

    private void setClickable(boolean clicked) {
        if (clicked) {
            binding.fbAngry.setClickable(false);
            binding.fbExcited.setClickable(false);
            binding.fbSad.setClickable(false);
            binding.fbHappy.setClickable(false);
            binding.fbWorried.setClickable(false);
        } else {
            binding.fbAngry.setClickable(true);
            binding.fbExcited.setClickable(true);
            binding.fbSad.setClickable(true);
            binding.fbHappy.setClickable(true);
            binding.fbWorried.setClickable(true);
        }
    }

    private void showAnimations(boolean clicked) {
        if (!clicked) {
            binding.fbAngry.startAnimation(fromBottom);
            binding.fbExcited.startAnimation(fromBottom);
            binding.fbSad.startAnimation(fromBottom);
            binding.fbHappy.startAnimation(fromBottom);
            binding.fbWorried.startAnimation(fromBottom);
        } else {
            binding.fbAngry.startAnimation(toBottom);
            binding.fbExcited.startAnimation(toBottom);
            binding.fbSad.startAnimation(toBottom);
            binding.fbHappy.startAnimation(toBottom);
            binding.fbWorried.startAnimation(toBottom);
        }
    }

    private void changeEmoji(FloatingActionButton v, FloatingActionButton previousV) {
        Drawable actualEmojiDrawable = v.getDrawable();
        if (binding.fbEmotions.getDrawable() != random) {
            v.setImageDrawable(previousV.getDrawable());
            previousV.setImageDrawable(binding.fbEmotions.getDrawable());
        }
        else{
            v.setImageDrawable(binding.fbEmotions.getDrawable());
             actualEmoji = Emojis.RANDOM;
        }
        binding.fbEmotions.setImageDrawable(actualEmojiDrawable);
        onFabEmotionClicked();
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
                .addToBackStack(null)
                .commit();

    }

    public String getEmotion() {
        Log.i("Emotion", actualEmoji.toString());
        if(binding.fbEmotions.getDrawable() == random){
            return Emojis.RANDOM.toString();
        }
        return actualEmoji.toString();
    }

    public void hideBottomAppBar(){
        binding.fbEmotions.hide();
        binding.btmAppBar.performHide();
    }

    public void showBottomAppBar(){
        binding.fbEmotions.show();
        binding.btmAppBar.performShow();
    }
}