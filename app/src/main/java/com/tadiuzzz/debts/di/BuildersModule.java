package com.tadiuzzz.debts.di;

import com.tadiuzzz.debts.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Simonov.vv on 19.06.2019.
 */
@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    // Add bindings for other sub-components here

}
