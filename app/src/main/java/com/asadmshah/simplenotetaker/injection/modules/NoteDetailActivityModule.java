package com.asadmshah.simplenotetaker.injection.modules;

import com.asadmshah.simplenotetaker.activities.NoteDetailActivity;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class NoteDetailActivityModule {

    private final NoteDetailActivity activity;

    public NoteDetailActivityModule(NoteDetailActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    NoteDetailActivity provideActivity() {
        return activity;
    }

}
