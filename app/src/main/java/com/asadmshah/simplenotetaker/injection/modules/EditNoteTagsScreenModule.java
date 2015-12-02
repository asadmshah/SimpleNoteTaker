package com.asadmshah.simplenotetaker.injection.modules;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.injection.scopes.ActivityScope;
import com.asadmshah.simplenotetaker.screens.editNoteTags.EditNoteTagsContract;
import com.asadmshah.simplenotetaker.screens.editNoteTags.EditNoteTagsPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class EditNoteTagsScreenModule {

    private final EditNoteTagsContract.Parent parent;
    private final EditNoteTagsContract.View view;
    private final long noteId;

    public EditNoteTagsScreenModule(EditNoteTagsContract.Parent parent, EditNoteTagsContract.View view, long noteId) {
        this.parent = parent;
        this.view = view;
        this.noteId = noteId;
    }

    @Provides
    @ActivityScope
    EditNoteTagsContract.Parent provideParent() {
        return parent;
    }

    @Provides
    @ActivityScope
    EditNoteTagsContract.View provideView() {
        return view;
    }

    @Provides
    @ActivityScope
    EditNoteTagsContract.Presenter providePresenter(EditNoteTagsContract.Parent parent, EditNoteTagsContract.View view, Database database) {
        return new EditNoteTagsPresenter(parent, view, database, noteId);
    }
}
