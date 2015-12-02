package com.asadmshah.simplenotetaker.injection.components;

import com.asadmshah.simplenotetaker.activities.EditNoteTagsActivity;
import com.asadmshah.simplenotetaker.injection.modules.EditNoteTagsActivityModule;
import com.asadmshah.simplenotetaker.injection.modules.EditNoteTagsScreenModule;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(
        modules = {
                EditNoteTagsActivityModule.class,
                EditNoteTagsScreenModule.class
        },
        dependencies = {BaseApplicationComponent.class}
)
public interface EditNoteTagsActivityComponent extends EditNoteTagsScreenComponent {
    void inject(EditNoteTagsActivity activity);
}
