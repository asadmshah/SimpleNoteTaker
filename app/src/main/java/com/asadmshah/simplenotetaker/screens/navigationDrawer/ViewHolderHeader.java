package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.asadmshah.simplenotetaker.R;

class ViewHolderHeader extends RecyclerView.ViewHolder {

    public final View viewAllNotes;

    public ViewHolderHeader(View itemView, final Callbacks listener) {
        super(itemView);
        viewAllNotes = itemView.findViewById(R.id.all_notes);
        viewAllNotes.setOnClickListener(v -> listener.onAllNotesSelected());
        itemView.findViewById(R.id.edit_tags).setOnClickListener(v -> listener.onEditTagsSelected());
    }

    interface Callbacks {

        void onAllNotesSelected();

        void onEditTagsSelected();
    }

}
