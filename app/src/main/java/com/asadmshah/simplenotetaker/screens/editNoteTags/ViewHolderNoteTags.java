package com.asadmshah.simplenotetaker.screens.editNoteTags;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asadmshah.simplenotetaker.R;
import com.asadmshah.simplenotetaker.widgets.RecyclerViewInteractions;

class ViewHolderNoteTags extends RecyclerView.ViewHolder {

    final ImageView viewIcon;
    final TextView viewLabel;
    final AppCompatCheckBox viewCheckbox;

    public ViewHolderNoteTags(View itemView, RecyclerViewInteractions.OnViewHolderClicked listener) {
        super(itemView);
        viewIcon = (ImageView) itemView.findViewById(R.id.icon);
        viewLabel = (TextView) itemView.findViewById(R.id.label);
        viewCheckbox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
        viewCheckbox.setClickable(false);

        itemView.setOnClickListener(v -> listener.onViewHolderClicked(this));
    }

}
