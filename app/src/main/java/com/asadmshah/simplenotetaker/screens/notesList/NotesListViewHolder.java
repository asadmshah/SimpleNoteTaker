package com.asadmshah.simplenotetaker.screens.notesList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.widgets.RecyclerViewInteractions;

class NotesListViewHolder extends RecyclerView.ViewHolder {

    final TextView viewTitle;
    final TextView viewText;

    public NotesListViewHolder(View itemView, final RecyclerViewInteractions.OnViewHolderClicked listener) {
        super(itemView);

        viewTitle = (TextView) itemView.findViewById(R.id.text_title);
        viewText = (TextView) itemView.findViewById(R.id.text_note);

        itemView.setOnClickListener(v -> {
            listener.onViewHolderClicked(NotesListViewHolder.this);
        });
    }

}
