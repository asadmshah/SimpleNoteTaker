package com.asadmshah.simplenotetaker.injection.modules;

import com.asadmshah.simplenotetaker.activities.EditNoteTagsActivity;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class EditNoteTagsActivityModule {

    private final EditNoteTagsActivity activity;

    public EditNoteTagsActivityModule(EditNoteTagsActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    EditNoteTagsActivity provideActivity() {
        return activity;
    }
}
