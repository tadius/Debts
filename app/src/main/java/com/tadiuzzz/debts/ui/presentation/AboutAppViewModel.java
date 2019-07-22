package com.tadiuzzz.debts.ui.presentation;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.BuildConfig;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import javax.inject.Inject;

/**
 * Created by Simonov.vv on 25.06.2019.
 */
public class AboutAppViewModel extends ViewModel {

    private final SingleLiveEvent<String> navigateSendEmailApp = new SingleLiveEvent<>();

    private ObservableField<String> appVersion = new ObservableField<>();
    private ObservableField<String> emailAuthor = new ObservableField<>();
    private ObservableField<String> authorName = new ObservableField<>();

    @Inject
    public AboutAppViewModel() {
        initData();
    }

    private void initData() {
        String version = BuildConfig.VERSION_NAME;
        String email = "tadiuzzz@gmail.com";
        String author = "Виталий Симонов";

        appVersion.set(version);
        emailAuthor.set(email);
        authorName.set(author);
    }

    public SingleLiveEvent<String> getNavigateSendEmailAppEvent() {
        return navigateSendEmailApp;
    }

    public ObservableField<String> getAppVersion() {
        return appVersion;
    }

    public ObservableField<String> getEmailAuthor() {
        return emailAuthor;
    }

    public ObservableField<String> getAuthorName() {
        return authorName;
    }

    public void clickedOnAuthorEmail() {
        navigateSendEmailApp.callWithArgument(getEmailAuthor().get());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
