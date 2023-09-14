package com.example.yourfamouscoach.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;

import com.example.yourfamouscoach.data.datasources.preferences.RxDataStoreManager;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SettingsScreen extends Fragment {

    //private FragmentSettingsScreenBinding binding;

    Preferences.Key<Boolean> NOTIFICATIONS_STATE = PreferencesKeys.booleanKey("notifications_state");
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
        //binding = FragmentSettingsScreenBinding.inflate(inflater, container, false);
        //initViews();


        return null;
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

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}