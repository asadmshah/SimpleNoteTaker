package com.asadmshah.simplenotetaker.screens.notesList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.widgets.RecyclerViewInteractions;

class Adapter extends RecyclerView.Adapter<NotesListViewHolder> {

    private DataSource dataSource;

    private RecyclerViewInteractions.OnViewHolderClicked onClickListener;

    public Adapter() {
        setHasStableIds(true);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setOnClickListener(RecyclerViewInteractions.OnViewHolderClicked onViewHolderClicked) {
        this.onClickListener = onViewHolderClicked;
    }

    @Override
    public long getItemId(int position) {
        return dataSource.getNote(position).id();
    }

    @Override
    public NotesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_holder_notes_list_item, parent, false);
        return new NotesListViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(NotesListViewHolder holder, int position) {
        holder.viewTitle.setText(dataSource.getNote(position).title());
        holder.viewText.setText(dataSource.getNote(position).text());
    }

    @Override
    public int getItemCount() {
        return dataSource != null ? dataSource.getCount() : 0;
    }

}
