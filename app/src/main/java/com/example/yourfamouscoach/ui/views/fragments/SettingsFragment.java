package com.example.yourfamouscoach.ui.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.PreferencesProto;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.work.WorkManager;

import com.example.yourfamouscoach.R;
import com.example.yourfamouscoach.data.datasources.preferences.RxDataStoreManager;
import com.example.yourfamouscoach.di.MyApplication;
import com.example.yourfamouscoach.ui.views.activitys.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SwitchPreferenceCompat notificationPref;
    private Boolean isNotifActive = true;

    private RxDataStoreManager preferencesManager;

    @Override
    public void onStop() {
        super.onStop();
        preferencesManager.saveNotifPref(isNotifActive);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        NavController navHostFragment = NavHostFragment.findNavController(this);
        preferencesManager = RxDataStoreManager.getInstance(this.requireContext());

        getPreferenceManager().findPreference("about_pref").setOnPreferenceClickListener(p -> {
            navHostFragment.navigate(R.id.action_settingsFragment_to_aboutScreenFragment);
            return true;
        });
        getPreferenceManager().findPreference("privacy_pref").setOnPreferenceClickListener(p -> {
            navHostFragment.navigate(R.id.action_settingsFragment_to_privacy_fragment);
            return true;
        });
        getPreferenceManager().findPreference("terms_pref").setOnPreferenceClickListener(p -> {
            navHostFragment.navigate(R.id.action_settingsFragment_to_termsAndConditionsFragments);
            return true;
        });
        notificationPref = getPreferenceManager().findPreference("notification_pref");
        if (notificationPref != null) {
            notificationPref.setOnPreferenceChangeListener((preference, newValue) -> {
                isNotifActive = false;
                if (newValue instanceof Boolean) {
                    isNotifActive = (Boolean) newValue;
                }
                return true;
            });
        }


    }
}