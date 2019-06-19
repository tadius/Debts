package com.tadiuzzz.debts;

import android.app.Activity;
import android.app.Application;

import com.tadiuzzz.debts.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by Simonov.vv on 19.06.2019.
 */
public class DebtsApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private static DebtsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
