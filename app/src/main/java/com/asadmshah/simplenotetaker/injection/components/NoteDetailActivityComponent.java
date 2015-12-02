package com.asadmshah.simplenotetaker.injection.components;

import com.asadmshah.simplenotetaker.activities.NoteDetailActivity;
import com.asadmshah.simplenotetaker.injection.modules.NoteDetailActivityModule;
import com.asadmshah.simplenotetaker.injection.modules.NoteDetailScreenModule;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(
        modules = {
                NoteDetailActivityModule.class,
                NoteDetailScreenModule.class
        },
        dependencies = {BaseApplicationComponent.class}
)
public interface NoteDetailActivityComponent extends NoteDetailScreenComponent {
    void inject(NoteDetailActivity activity);
}
