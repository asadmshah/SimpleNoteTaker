package com.asadmshah.simplenotetaker.screens.editNoteTags;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.widgets.RecyclerViewInteractions.OnViewHolderClicked;

class NoteTagsAdapter extends RecyclerView.Adapter<ViewHolderNoteTags> implements OnViewHolderClicked {

    private NoteTagsDataSource dataSource;
    private OnViewHolderClicked onViewHolderClickListener;

    public NoteTagsAdapter() {
        setHasStableIds(true);
    }

    public void setDataSource(NoteTagsDataSource dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    public void setOnViewHolderClickListener(OnViewHolderClicked listener) {
        this.onViewHolderClickListener = listener;
    }

    @Override
    public long getItemId(int position) {
        return dataSource.getNoteTag(position).getTag().id();
    }

    @Override
    public ViewHolderNoteTags onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_note_tags, parent, false);
        return new ViewHolderNoteTags(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolderNoteTags holder, int position) {
        holder.viewIcon.setColorFilter(dataSource.getNoteTag(position).getTag().color());
        holder.viewLabel.setText(dataSource.getNoteTag(position).getTag().label());
        holder.viewCheckbox.setChecked(dataSource.getNoteTag(position).isTagged());
    }

    @Override
    public int getItemCount() {
        return dataSource != null ? dataSource.getCount() : 0;
    }

    @Override
    public void onViewHolderClicked(RecyclerView.ViewHolder viewHolder) {
        if (onViewHolderClickListener != null) {
            onViewHolderClickListener.onViewHolderClicked(viewHolder);
        }
    }
}
