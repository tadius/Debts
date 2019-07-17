package com.tadiuzzz.debts.di;

import android.app.Application;

import com.tadiuzzz.debts.DebtsApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Simonov.vv on 19.06.2019.
 */
@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,


        }
)
public interface AppComponent extends AndroidInjector<DebtsApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(DebtsApplication debtsApplication);
}
