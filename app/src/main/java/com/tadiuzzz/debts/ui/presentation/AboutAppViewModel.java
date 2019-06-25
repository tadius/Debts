package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.BuildConfig;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

/**
 * Created by Simonov.vv on 25.06.2019.
 */
public class AboutAppViewModel extends AndroidViewModel {
    private final SingleLiveEvent<String> navigateSendEmailApp = new SingleLiveEvent<>();

    private final MutableLiveData<String> appVersion = new MutableLiveData<>();
    private final MutableLiveData<String> emailAuthor = new MutableLiveData<>();

    private final MutableLiveData<String> titleBarName = new MutableLiveData<>();

    public AboutAppViewModel(@NonNull Application application) {
        super(application);

        initData();
    }

    private void initData() {
        String version = BuildConfig.VERSION_NAME;
        String email = "tadiuzzz@gmail.com";
        String title = "О программе";

        appVersion.setValue(version);
        emailAuthor.setValue(email);
        titleBarName.setValue(title);
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

    public LiveData<String> getTitleBarName() {
        return titleBarName;
    }

    public void clickedOnAuthorEmail() {
        navigateSendEmailApp.callWithArgument(getEmailAuthor().getValue());
    }
}
