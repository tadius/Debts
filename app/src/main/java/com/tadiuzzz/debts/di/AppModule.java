package com.tadiuzzz.debts.di;

import android.content.Context;

import com.tadiuzzz.debts.DebtsApplication;
import com.tadiuzzz.debts.data.DebtRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Simonov.vv on 19.06.2019.
 */
@Module
public class AppModule {

    // к данным объектам будут иметь доступ абсолютно все

    @Provides
    Context provideContext(DebtsApplication application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    DebtRepository provideDebtRepository(DebtsApplication application) {
        return new DebtRepository(application);
    }
}
