package com.tadiuzzz.debts.di;

import com.tadiuzzz.debts.DebtsApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Simonov.vv on 19.06.2019.
 */
@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BuildersModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(DebtsApplication application);
        AppComponent build();
    }

    void inject(DebtsApplication debtsApplication);
}
