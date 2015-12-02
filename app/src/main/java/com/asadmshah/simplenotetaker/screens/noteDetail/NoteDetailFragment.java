package com.asadmshah.simplenotetaker.screens.noteDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;

import javax.inject.Inject;

public class NoteDetailFragment extends Fragment implements NoteDetailContract.View {

    public static final String TAG = NoteDetailFragment.class.getSimpleName();

    @Inject NoteDetailContract.Presenter presenter;

    private EditText viewEditTitle;
    private EditText viewEditNote;
    private ViewGroup viewTagsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);

        viewEditTitle = (EditText) view.findViewById(R.id.text_title);
        viewEditNote = (EditText) view.findViewById(R.id.text_note);
        viewTagsList = (ViewGroup) view.findViewById(R.id.tags_list);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadNote();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribeFromNoteUpdates();
        presenter.updateNote(viewEditTitle.getText().toString(), viewEditNote.getText().toString());
    }

    @Override
    public void noteLoaded(Note note) {
        viewEditTitle.setText(note.title());
        viewEditNote.setText(note.text());
    }

    @Override
    public void setNoteStateEnabled(boolean enabled) {
        viewEditNote.setEnabled(enabled);
        viewEditTitle.setEnabled(enabled);
    }

    @Override
    public void setNoteStateLoading(boolean isLoadingState) {

    }

    @Override
    public void showError(ErrorConstants error) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, error.getString(getActivity()), Snackbar.LENGTH_LONG).show();
        }
    }
}
