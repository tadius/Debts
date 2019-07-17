package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.BuildConfig;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import javax.inject.Inject;

/**
 * Created by Simonov.vv on 25.06.2019.
 */
public class AboutAppViewModel extends ViewModel {

    private final SingleLiveEvent<String> navigateSendEmailApp = new SingleLiveEvent<>();

    private final MutableLiveData<String> appVersion = new MutableLiveData<>();
    private final MutableLiveData<String> emailAuthor = new MutableLiveData<>();


    @Inject
    public AboutAppViewModel() {
        initData();
    }

    private void initData() {
        String version = BuildConfig.VERSION_NAME;
        String email = "tadiuzzz@gmail.com";

        appVersion.setValue(version);
        emailAuthor.setValue(email);
    }

    public SingleLiveEvent<String> getNavigateSendEmailAppEvent() {
        return navigateSendEmailApp;
    }

    public LiveData<String> getAppVersion() {
        return appVersion;
    }

    public LiveData<String> getEmailAuthor() {
        return emailAuthor;
    }

    public void clickedOnAuthorEmail() {
        navigateSendEmailApp.callWithArgument(getEmailAuthor().getValue());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
