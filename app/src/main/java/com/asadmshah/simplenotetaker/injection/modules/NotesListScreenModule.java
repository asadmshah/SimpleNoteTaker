package com.asadmshah.simplenotetaker.injection.modules;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.notesList.NotesListContract;
import com.asadmshah.simplenotetaker.screens.notesList.NotesListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NotesListScreenModule {

    private final NotesListContract.Parent parent;
    private final NotesListContract.View view;

    public NotesListScreenModule(NotesListContract.Parent parent, NotesListContract.View view) {
        this.parent = parent;
        this.view = view;
    }

    @Provides
    @ActivityScope
    NotesListContract.Parent provideParent() {
        return parent;
    }

    @Provides
    @ActivityScope
    NotesListContract.View provideView() {
        return view;
    }

    @Provides
    @ActivityScope
    NotesListContract.Presenter providePresenter(NotesListContract.Parent parent, NotesListContract.View view, Database database) {
        return new NotesListPresenter(view, parent, database);
    }

}
