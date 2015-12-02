package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asadmshah.simplenotetaker.R;

class ViewHolderTag extends RecyclerView.ViewHolder {

    final ImageView viewIcon;
    final TextView viewLabel;

    public ViewHolderTag(View itemView, Callbacks listener) {
        super(itemView);
        viewIcon = (ImageView) itemView.findViewById(R.id.icon);
        viewLabel = (TextView) itemView.findViewById(R.id.label);
        itemView.setOnClickListener(v -> listener.onTagSelected(this));
    }

    interface Callbacks {

        void onTagSelected(ViewHolderTag viewHolder);
    }

}
