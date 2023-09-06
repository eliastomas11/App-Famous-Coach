package com.example.yourfamouscoach.ui.views.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxDataStoreBuilder;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.data.datasources.preferences.RxDataStoreManager;
import com.example.yourfamouscoach.databinding.FragmentSettingsScreenBinding;
import com.example.yourfamouscoach.di.MyApplication;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SettingsScreen extends Fragment {

    private FragmentSettingsScreenBinding binding;

    Preferences.Key<Boolean> NOTIFICATIONS_STATE = PreferencesKeys.booleanKey("notifications_state");
    Preferences.Key<Boolean> NIGHT_MODE_STATE = PreferencesKeys.booleanKey("theme_state");
    RxDataStore<Preferences> dataStore;

    int flag = 0;


    public SettingsScreen() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataStore = RxDataStoreManager.getInstance(requireContext().getApplicationContext()).getDataStore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsScreenBinding.inflate(inflater, container, false);
        initViews();

        binding.swNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSetting(NIGHT_MODE_STATE, isChecked);
            }
        });

        binding.swNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSetting(NOTIFICATIONS_STATE, isChecked);
            }
        });


        return binding.getRoot();
    }

    private <T> void saveSetting(Preferences.Key<T> key, T setting) {
        Single<Preferences> updateResult = dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(key, setting);
            return Single.just(mutablePreferences);
        }).subscribeOn(Schedulers.io());
    }

    private void initViews() {
        dataStore.data().map(prefs -> prefs.get(NOTIFICATIONS_STATE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        notyStateShow(aBoolean);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        dataStore.data().map(prefs -> prefs.get(NIGHT_MODE_STATE)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        nightTeme(aBoolean);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void nightTeme(Boolean nightMode) {
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            binding.swNightMode.setChecked(true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            binding.swNightMode.setChecked(false);
        }
    }

    private void notyStateShow(Boolean aBoolean) {
        binding.swNotifications.setChecked(aBoolean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}