package com.tadiuzzz.debts.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.tadiuzzz.debts.R;

import java.util.ArrayList;

/**
 * Created by Simonov.vv on 03.09.2019.
 */
public class PreferencesFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        setKeyClickListeners();
        configPreferences();
    }

    private void setKeyClickListeners() {
        ArrayList<String> preferenceKeys = new ArrayList<>();
        preferenceKeys.add("key_manual_backup");
        preferenceKeys.add("key_about");

        for (String preferenceKey : preferenceKeys) {
            Preference preference = findPreference(preferenceKey);
            if (preference != null) {
                preference.setOnPreferenceClickListener(this);
            }
        }
    }

    private void configPreferences() {
        //Выделение всего текста в EditText при появлении диалога выбора интервала
        EditIntegerPreference preferenceInterval = findPreference("pref_interval_int");
        if (preferenceInterval != null) {
            preferenceInterval.setOnBindEditTextListener(EditText::selectAll);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Activity activity = getActivity();
        if (activity != null) {
            switch (preference.getKey()) {
                case "key_manual_backup":
                    Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_preferencesFragment_to_backupRestoreFragment);
                    return true;
                case "key_about":
                    Navigation.findNavController(activity, R.id.nav_host_fragment).navigate(R.id.action_preferencesFragment_to_aboutAppFragment);
                    return true;
            }
        }
        return false;
    }
}
