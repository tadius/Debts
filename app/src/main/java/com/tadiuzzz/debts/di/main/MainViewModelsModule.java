package com.tadiuzzz.debts.di.main;

import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.MainViewModel;
import com.tadiuzzz.debts.di.ViewModelKey;
import com.tadiuzzz.debts.ui.presentation.AboutAppViewModel;
import com.tadiuzzz.debts.ui.presentation.BackupRestoreViewModel;
import com.tadiuzzz.debts.ui.presentation.CategoriesViewModel;
import com.tadiuzzz.debts.ui.presentation.DebtsViewModel;

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

    @Binds
    @IntoMap
    @ViewModelKey(AboutAppViewModel.class)
    public abstract ViewModel bindAboutAppViewModel(AboutAppViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BackupRestoreViewModel.class)
    public abstract ViewModel bindBackupRestoreViewModel(BackupRestoreViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel.class)
    public abstract ViewModel bindCategoriesViewModel(CategoriesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DebtsViewModel.class)
    public abstract ViewModel bindDebtsViewModel(DebtsViewModel viewModel);
}
