package com.example.yourfamouscoach.data.datasources.preferences;

import android.content.Context;

import androidx.datastore.core.DataStoreFactory;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxDataStoreBuilder;

public class RxDataStoreManager {

    private static RxDataStoreManager instance;
    private final RxDataStore<Preferences> rxDataStore;

    private final String DATASTORE_NAME = "settings";


    private RxDataStoreManager(Context context) {
        rxDataStore = new RxPreferenceDataStoreBuilder(context,DATASTORE_NAME).build();
    }

    public static RxDataStoreManager getInstance(Context context) {
        if (instance == null) {
            synchronized (RxDataStoreManager.class) {
                if (instance == null) {
                    instance = new RxDataStoreManager(context);
                }
            }
        }
        return instance;
    }

    public RxDataStore<Preferences> getDataStore() {
        return rxDataStore;
    }
}
