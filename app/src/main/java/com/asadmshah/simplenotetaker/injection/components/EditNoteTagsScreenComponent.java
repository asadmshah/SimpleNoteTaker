package com.asadmshah.simplenotetaker.injection.components;

import com.asadmshah.simplenotetaker.injection.modules.EditNoteTagsScreenModule;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.editNoteTags.EditNoteTagsContract;
import com.asadmshah.simplenotetaker.screens.editNoteTags.EditNoteTagsFragment;

import dagger.Component;

@ActivityScope
@Component(
        modules = {EditNoteTagsScreenModule.class},
        dependencies = {BaseApplicationComponent.class}
)
public interface EditNoteTagsScreenComponent {
    void inject(EditNoteTagsFragment fragment);

    EditNoteTagsContract.Parent editNoteTagsParent();
    EditNoteTagsContract.View editNoteTagsView();
    EditNoteTagsContract.Presenter editNoteTagsPresenter();
}
