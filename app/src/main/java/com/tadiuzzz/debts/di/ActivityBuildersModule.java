package com.tadiuzzz.debts.di;

import com.tadiuzzz.debts.MainActivity;
import com.tadiuzzz.debts.di.main.MainFragmentBuildersModule;
import com.tadiuzzz.debts.di.main.MainViewModelsModule;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Simonov.vv on 16.07.2019.
 */
@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
            modules = {
                    MainViewModelsModule.class,
                    MainFragmentBuildersModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

}
