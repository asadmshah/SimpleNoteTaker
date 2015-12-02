package com.asadmshah.simplenotetaker.screens.noteDetail;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NoteDetailPresenter implements NoteDetailContract.Presenter {

    private final NoteDetailContract.View view;
    private final NoteDetailContract.Parent parent;
    private final Database database;
    private final long noteId;

    private Subscription noteSubscription;
    private Subscription tagsSubscription;

    public NoteDetailPresenter(NoteDetailContract.View view, NoteDetailContract.Parent parent, Database database, long noteId) {
        this.view = view;
        this.parent = parent;
        this.database = database;
        this.noteId = noteId;
    }

    @Override
    public void loadNote() {
        unSubscribeFromNoteUpdates();
        view.setNoteStateEnabled(false);
        view.setNoteStateLoading(true);
        noteSubscription = database.getNote(noteId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Note note) -> {
                    if (note != null) {
                        view.noteLoaded(note);
                        view.setNoteStateEnabled(true);
                    } else {
                        view.showError(ErrorConstants.NOTE_DOES_NOT_EXIST);
                        view.setNoteStateEnabled(false);
                    }
                    view.setNoteStateLoading(false);
                });
    }

    @Override
    public void updateNote(String title, String text) {
        if (!database.updateNote(noteId, title, text)) {
            view.showError(ErrorConstants.UNABLE_TO_UPDATE_NOTE);
        }
    }

    @Override
    public void deleteNote() {
        unSubscribeFromNoteUpdates();
        view.setNoteStateEnabled(false);
        if (!database.deleteNote(noteId)) {
            view.showError(ErrorConstants.UNABLE_TO_DELETE_NOTE);
        }
        parent.closeNoteDetailScreen();
    }

    @Override
    public void editTags() {
        parent.requestEditTagsScreen(noteId);
    }

    @Override
    public void unSubscribeFromNoteUpdates() {
        if (noteSubscription != null && !noteSubscription.isUnsubscribed()) {
            noteSubscription.unsubscribe();
        }
    }

}
