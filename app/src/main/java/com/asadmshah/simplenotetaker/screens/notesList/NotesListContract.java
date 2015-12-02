package com.asadmshah.simplenotetaker.screens.notesList;

import com.asadmshah.simplenotetaker.utils.ErrorConstants;

public interface NotesListContract {

    interface View {

        void notesLoaded();

        void setNotesListStateLoading(boolean isLoadingState);

        void showError(ErrorConstants error);
    }

    interface Presenter {

        long getTagId();

        DataSource getDataSource();

        void reloadNotes();

        void reloadNotes(long tagId);

        void onAddNewNoteClicked();

        void onNoteSelected(long noteId);

        void unSubscribeFromUpdates();
    }

    interface Parent {

        void showNote(long id);
    }

}
