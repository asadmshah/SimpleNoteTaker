package com.asadmshah.simplenotetaker.injection.modules;

import com.asadmshah.simplenotetaker.activities.MainActivity;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    private final MainActivity activity;

    public MainActivityModule(MainActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    MainActivity provideActivity() {
        return activity;
    }

}
