package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.asadmshah.simplenotetaker.R;

class ViewHolderFooter extends RecyclerView.ViewHolder {

    public ViewHolderFooter(View itemView, final Callbacks listener) {
        super(itemView);
        itemView.findViewById(R.id.create_new_tag).setOnClickListener(v -> listener.onCreateNewTagSelected());
        itemView.findViewById(R.id.settings).setOnClickListener(v -> listener.onSettingsSelected());
    }

    interface Callbacks {

        void onCreateNewTagSelected();

        void onSettingsSelected();
    }

}
