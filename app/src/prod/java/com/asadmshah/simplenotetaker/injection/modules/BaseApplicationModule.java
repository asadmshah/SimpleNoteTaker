package com.asadmshah.simplenotetaker.injection.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.asadmshah.simplenotetaker.BaseApplication;
import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.database.ProviderBackedDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseApplicationModule {

    private final BaseApplication application;

    public BaseApplicationModule(BaseApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    Database provideDatabase(Context context) {
        return new ProviderBackedDatabase(context.getContentResolver());
    }

}
