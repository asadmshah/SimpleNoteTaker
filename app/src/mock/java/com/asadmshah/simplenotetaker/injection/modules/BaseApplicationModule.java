package com.asadmshah.simplenotetaker.injection.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.asadmshah.simplenotetaker.BaseApplication;
import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.database.MockData;
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
        ProviderBackedDatabase database = new ProviderBackedDatabase(context.getContentResolver());
        MockData.insertedNotes.add(database.insertNote(MockData.Note1.TITLE, MockData.Note1.TEXT));
        MockData.insertedNotes.add(database.insertNote(MockData.Note2.TITLE, MockData.Note2.TEXT));
        MockData.insertedTags.add(database.insertTag(MockData.Tag1.LABEL, MockData.Tag1.COLOR));
        MockData.insertedTags.add(database.insertTag(MockData.Tag2.LABEL, MockData.Tag2.COLOR));
        MockData.insertedTags.add(database.insertTag(MockData.Tag3.LABEL, MockData.Tag3.COLOR));
        return database;
    }

}
