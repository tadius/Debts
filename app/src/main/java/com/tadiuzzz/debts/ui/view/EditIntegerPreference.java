package com.tadiuzzz.debts.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceDataStore;

import com.tadiuzzz.debts.R;


/**
 * Created by Simonov.vv on 05.09.2019.
 */
public class EditIntegerPreference extends EditTextPreference {

    private static final int DEFAULT_VALUE = 0;
    private static final String DEFAULT_VALUE_TEXT = "0";

    public EditIntegerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_dialog_edittext_int); //Устанавливаем свой Layout диалога
    }

    //Вызывается для получения значения из SharedPreference
    @Override
    public String getText() {
        return String.valueOf(getSharedPreferences().getInt(getKey(), DEFAULT_VALUE));
    }

    //Вызывается для записи значения в SharedPreference
    @Override
    public void setText(String text) {
        if (text != null && !text.equals("") && Integer.parseInt(text) != 0) {
            final boolean wasBlocking = shouldDisableDependents();

            persistInt(Integer.parseInt(text));

            final boolean isBlocking = shouldDisableDependents();
            if (isBlocking != wasBlocking) {
                notifyDependencyChange(isBlocking);
            }

            notifyChanged();
        }
    }

    //Вызывается для установки значения в EditText и в Summary
    @Override
    protected String getPersistedString(String defaultReturnValue) {
        if (!shouldPersist() && defaultReturnValue != null && !defaultReturnValue.equals("")) {
            return defaultReturnValue;
        }

        PreferenceDataStore dataStore = getPreferenceDataStore();
        if (dataStore != null) {
            return String.valueOf(dataStore.getInt(getKey(), Integer.parseInt(defaultReturnValue != null ? defaultReturnValue : DEFAULT_VALUE_TEXT)));
        }

        return String.valueOf(getSharedPreferences().getInt(getKey(), Integer.parseInt(defaultReturnValue != null ? defaultReturnValue : DEFAULT_VALUE_TEXT)));

    }
}
