package com.tadiuzzz.debts.di;

import android.app.Application;
import android.content.Context;

import com.tadiuzzz.debts.DebtsApplication;
import com.tadiuzzz.debts.data.CacheEditing;
import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.utils.SortingManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Simonov.vv on 19.06.2019.
 */
@Module
public class AppModule {

    // к данным объектам будут иметь доступ абсолютно все

    @Singleton
    @Provides
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    SortingManager provideSortingManager() {
        return new SortingManager();
    }

    @Singleton
    @Provides
    CacheEditing provideCacheEditing() {
        return new CacheEditing();
    }

    @Singleton
    @Provides
    DebtRepository provideDebtRepository(Application application, CacheEditing cacheEditing) {
        return new DebtRepository(application, cacheEditing);
    }
}
