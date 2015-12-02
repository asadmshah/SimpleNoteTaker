package com.asadmshah.simplenotetaker.widgets;

import android.support.v7.widget.RecyclerView;

public interface RecyclerViewInteractions {

    interface OnViewHolderClicked {
        void onViewHolderClicked(RecyclerView.ViewHolder viewHolder);
    }

}
