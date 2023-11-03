package com.example.yourfamouscoach.data.datasources.preferences;

import android.content.Context;

import androidx.datastore.core.DataStoreFactory;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxDataStoreBuilder;

import java.sql.Timestamp;
import java.time.Instant;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class RxDataStoreManager {
    private static RxDataStoreManager INSTANCE;
    private RxDataStore<Preferences> dataStore;

    public static final String notificationPrefKey = "NOTIFICATION_STATE";

    private Preferences.Key<String> TIME_STAMP_KEY = new Preferences.Key<>("timestamp");

    private final String DATASTORE_NAME = "settings";

    Preferences.Key<Boolean> NOTIF_PREF = PreferencesKeys.booleanKey(notificationPrefKey);


    private RxDataStoreManager(Context context) {
        dataStore = new RxPreferenceDataStoreBuilder(context,DATASTORE_NAME ).build();
    }

    public static synchronized RxDataStoreManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RxDataStoreManager(context);
        }
        return INSTANCE;
    }

    private String getTimeStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    public Single<Preferences> saveTimeStampCacheRefresh(){
        return dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(TIME_STAMP_KEY, getTimeStamp());
            return Single.just(mutablePreferences);
        });
    }

    public void saveNotifPref(Boolean value){
        dataStore.updateDataAsync(prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(NOTIF_PREF,value);
            return Single.just(mutablePreferences);
        });

    }

    public Flowable<Boolean> getNotifPref(){
        Flowable<Boolean> notifPref = dataStore.data().map(prefs -> prefs.get(NOTIF_PREF));
        return notifPref;
    }

}
