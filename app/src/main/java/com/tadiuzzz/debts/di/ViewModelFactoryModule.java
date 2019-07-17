package com.tadiuzzz.debts.di;

import androidx.lifecycle.ViewModelProvider;

import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Simonov.vv on 16.07.2019.
 */
@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);


}
