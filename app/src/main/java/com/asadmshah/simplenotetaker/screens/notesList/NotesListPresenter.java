package com.asadmshah.simplenotetaker.screens.notesList;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.models.Note;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotesListPresenter implements NotesListContract.Presenter {

    private final NotesListContract.View view;
    private final NotesListContract.Parent parent;
    private final Database database;
    private final DataSource dataSource = new DataSource() {
        @Override
        public Note getNote(int position) {
            return notesList.get(position);
        }

        @Override
        public int getCount() {
            return notesList != null ? notesList.size() : 0;
        }
    };

    private long tagId = -1;
    private List<Note> notesList;
    private Subscription notesSubscription;

    public NotesListPresenter(NotesListContract.View view, NotesListContract.Parent parent, Database database) {
        this.view = view;
        this.parent = parent;
        this.database = database;
    }

    @Override
    public long getTagId() {
        return tagId;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void reloadNotes() {
        this.tagId = -1;
        unSubscribeFromUpdates();
        view.setNotesListStateLoading(true);
        notesSubscription = database.getNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> {
                    notesList = notes;
                    view.setNotesListStateLoading(false);
                    view.notesLoaded();
                });
    }

    @Override
    public void reloadNotes(long tagId) {
        this.tagId = tagId;
        unSubscribeFromUpdates();
        view.setNotesListStateLoading(true);
        notesSubscription = database.getNotesOfTag(tagId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> {
                    notesList = notes;
                    view.setNotesListStateLoading(false);
                    view.notesLoaded();
                });
    }

    @Override
    public void onAddNewNoteClicked() {
        Note note = database.insertNote("", "");
        parent.showNote(note.id());
    }

    @Override
    public void onNoteSelected(long noteId) {
        parent.showNote(noteId);
    }

    @Override
    public void unSubscribeFromUpdates() {
        if (notesSubscription != null && !notesSubscription.isUnsubscribed()) {
            notesSubscription.unsubscribe();
        }
    }

}
