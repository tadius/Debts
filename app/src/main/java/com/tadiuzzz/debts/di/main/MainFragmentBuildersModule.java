package com.tadiuzzz.debts.di.main;

import com.tadiuzzz.debts.ui.view.AboutAppFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Simonov.vv on 17.07.2019.
 */
@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract AboutAppFragment contributeAboutAppFragment();
}
