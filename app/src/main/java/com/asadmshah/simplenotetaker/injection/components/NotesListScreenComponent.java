package com.asadmshah.simplenotetaker.injection.components;

import com.asadmshah.simplenotetaker.injection.modules.NotesListScreenModule;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.notesList.NotesListContract;
import com.asadmshah.simplenotetaker.screens.notesList.NotesListFragment;

import dagger.Component;

@ActivityScope
@Component(
        modules = {NotesListScreenModule.class},
        dependencies = {BaseApplicationComponent.class}
)
public interface NotesListScreenComponent {
    void inject(NotesListFragment view);

    NotesListContract.View notesListView();
    NotesListContract.Parent notesListParent();
    NotesListContract.Presenter notesListPresenter();
}
