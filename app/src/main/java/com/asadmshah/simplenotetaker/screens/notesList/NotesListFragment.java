package com.asadmshah.simplenotetaker.screens.notesList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.utils.ErrorConstants;
import com.asadmshah.simplenotetaker.widgets.RecyclerViewInteractions;

import javax.inject.Inject;

public class NotesListFragment extends Fragment implements NotesListContract.View, RecyclerViewInteractions.OnViewHolderClicked {

    public static final String TAG = NotesListFragment.class.getSimpleName();

    @Inject NotesListContract.Presenter presenter;

    private Adapter adapter;
    private RecyclerView viewList;
    private FloatingActionButton viewFab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new Adapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        viewList = (RecyclerView) view.findViewById(R.id.notes_list);
        viewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewList.setAdapter(adapter);

        viewFab = (FloatingActionButton) view.findViewById(R.id.fab_add_notes);
        viewFab.setOnClickListener((View v) -> {
            presenter.onAddNewNoteClicked();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.reloadNotes();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribeFromUpdates();
    }

    @Override
    public void notesLoaded() {
        adapter.setDataSource(presenter.getDataSource());
        viewList.swapAdapter(adapter, true);
    }

    @Override
    public void setNotesListStateLoading(boolean isLoadingState) {
        adapter.setOnClickListener(isLoadingState ? null : this);
    }

    @Override
    public void showError(ErrorConstants error) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, error.getString(getActivity()), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onViewHolderClicked(RecyclerView.ViewHolder viewHolder) {
        presenter.onNoteSelected(viewHolder.getItemId());
    }
}
