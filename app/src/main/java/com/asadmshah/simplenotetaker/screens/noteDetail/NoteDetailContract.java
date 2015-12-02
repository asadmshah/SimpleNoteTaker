package com.asadmshah.simplenotetaker.screens.noteDetail;

import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;

public interface NoteDetailContract {

    interface View {

        void noteLoaded(Note note);

        void setNoteStateEnabled(boolean enabled);

        void setNoteStateLoading(boolean isLoadingState);

        void showError(ErrorConstants error);
    }

    interface Presenter {

        void loadNote();

        void updateNote(String title, String text);

        void deleteNote();

        void editTags();

        void unSubscribeFromNoteUpdates();
    }

    interface Parent {

        void closeNoteDetailScreen();

        void requestEditTagsScreen(long noteId);
    }

}
