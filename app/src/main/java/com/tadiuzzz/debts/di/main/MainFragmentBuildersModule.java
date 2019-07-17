package com.tadiuzzz.debts.di.main;

import com.tadiuzzz.debts.ui.view.AboutAppFragment;
import com.tadiuzzz.debts.ui.view.BackupRestoreFragment;
import com.tadiuzzz.debts.ui.view.CategoriesFragment;
import com.tadiuzzz.debts.ui.view.DebtsFragment;
import com.tadiuzzz.debts.ui.view.EditCategoryFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Simonov.vv on 17.07.2019.
 */
@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract AboutAppFragment contributeAboutAppFragment();

    @ContributesAndroidInjector
    abstract BackupRestoreFragment contributeBackupRestoreFragment();

    @ContributesAndroidInjector
    abstract CategoriesFragment contributeCategoriesFragment();

    @ContributesAndroidInjector
    abstract DebtsFragment contributeDebtsFragment();

    @ContributesAndroidInjector
    abstract EditCategoryFragment contributeEditCategoryFragment();
}
