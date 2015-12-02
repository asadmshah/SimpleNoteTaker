package com.asadmshah.simplenotetaker.injection.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.injection.modules.BaseApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {BaseApplicationModule.class}
)
public interface BaseApplicationComponent {
    Context context();
    SharedPreferences sharedPreferences();
    Database database();
}
