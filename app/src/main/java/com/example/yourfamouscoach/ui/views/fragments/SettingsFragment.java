package com.example.yourfamouscoach.ui.views.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.PreferencesProto;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.yourfamouscoach.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        getPreferenceManager().findPreference("about_pref").setOnPreferenceClickListener( p -> changeScreen(AboutScreen.class,null));
        getPreferenceManager().findPreference("privacy_pref").setOnPreferenceClickListener( p -> changeScreen(PrivacyPolicy.class,null));
        getPreferenceManager().findPreference("terms_pref").setOnPreferenceClickListener( p -> changeScreen(TermsAndConditions.class,null));

    }

    private <T extends Fragment> boolean changeScreen(Class<T> tClass, Bundle args) {
        getParentFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, tClass, args)
                .addToBackStack(null)
                .commit();
        return true;
    }

}