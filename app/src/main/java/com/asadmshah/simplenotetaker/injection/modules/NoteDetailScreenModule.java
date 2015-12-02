package com.asadmshah.simplenotetaker.injection.modules;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.noteDetail.NoteDetailContract;
import com.asadmshah.simplenotetaker.screens.noteDetail.NoteDetailPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NoteDetailScreenModule {

    private final NoteDetailContract.Parent parent;
    private final NoteDetailContract.View view;
    private final long noteId;

    public NoteDetailScreenModule(NoteDetailContract.Parent parent, NoteDetailContract.View view, long noteId) {
        this.parent = parent;
        this.view = view;
        this.noteId = noteId;
    }

    @Provides
    @ActivityScope
    NoteDetailContract.Parent provideParent() {
        return parent;
    }

    @Provides
    @ActivityScope
    NoteDetailContract.View provideView() {
        return view;
    }

    @Provides
    @ActivityScope
    NoteDetailContract.Presenter providePresenter(NoteDetailContract.Parent parent, NoteDetailContract.View view, Database database) {
        return new NoteDetailPresenter(view, parent, database, noteId);
    }
}
