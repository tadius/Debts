package com.tadiuzzz.debts;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;


/**
 * Created by Simonov.vv on 16.07.2019.
 */
public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    @Inject
    public MainViewModel() {
        Log.i(TAG, "MainViewModel: OLOLOL");
    }
}
