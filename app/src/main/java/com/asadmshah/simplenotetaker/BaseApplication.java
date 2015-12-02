package com.asadmshah.simplenotetaker;

import android.app.Application;
import android.content.Context;

import com.asadmshah.simplenotetaker.injection.components.BaseApplicationComponent;
import com.asadmshah.simplenotetaker.injection.components.DaggerBaseApplicationComponent;
import com.asadmshah.simplenotetaker.injection.modules.BaseApplicationModule;

import timber.log.Timber;

public class BaseApplication extends Application {

    private BaseApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        component = DaggerBaseApplicationComponent.builder()
                .baseApplicationModule(new BaseApplicationModule(this))
                .build();
    }

    public static BaseApplicationComponent getComponent(Context context) {
        return ((BaseApplication) context.getApplicationContext()).component;
    }

}
