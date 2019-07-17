package com.tadiuzzz.debts.di.main;

import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.MainViewModel;
import com.tadiuzzz.debts.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Simonov.vv on 16.07.2019.
 */
@Module
public abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);
}
