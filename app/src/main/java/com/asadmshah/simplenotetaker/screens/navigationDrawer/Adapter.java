package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asadmshah.simplenotetaker.R;

class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderHeader.Callbacks,
        ViewHolderTag.Callbacks, ViewHolderFooter.Callbacks {

    private static final int VIEW_TYPE_TAG = 0;
    private static final int VIEW_TYPE_HEADER = 1;
    private static final int VIEW_TYPE_FOOTER = 2;

    private DataSource dataSource;
    private Callbacks callbacks;

    public Adapter() {
        setHasStableIds(true);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public long getItemId(int position) {
        return (position == 0 || position == getItemCount()-1) ? -1 : dataSource.getTag(position-1).id();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        }
        if (position == getItemCount()-1) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_TAG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                view = inflater.inflate(R.layout.view_holder_navigation_drawer_header, parent, false);
                viewHolder = new ViewHolderHeader(view, this);
                break;
            case VIEW_TYPE_FOOTER:
                view = inflater.inflate(R.layout.view_holder_navigation_drawer_footer, parent, false);
                viewHolder = new ViewHolderFooter(view, this);
                break;
            default:
                view = inflater.inflate(R.layout.view_holder_navigation_drawer_tag, parent, false);
                viewHolder = new ViewHolderTag(view, this);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_TAG) {
            ViewHolderTag vh = (ViewHolderTag) holder;
            vh.viewIcon.setColorFilter(dataSource.getTag(position-1).color());
            vh.viewLabel.setText(dataSource.getTag(position-1).label());
        }
    }

    @Override
    public int getItemCount() {
        return (dataSource != null ? dataSource.getCount() : 0) + 2;
    }

    @Override
    public void onAllNotesSelected() {
        if (callbacks != null) {
            callbacks.onAllNotesSelected();
        }
    }

    @Override
    public void onEditTagsSelected() {
        if (callbacks != null) {
            callbacks.onEditTagsSelected();
        }
    }

    @Override
    public void onTagSelected(ViewHolderTag viewHolder) {
        if (callbacks != null) {
            callbacks.onTagSelected(viewHolder);
        }
    }

    @Override
    public void onCreateNewTagSelected() {
        if (callbacks != null) {
            callbacks.onCreateNewTagSelected();
        }
    }

    @Override
    public void onSettingsSelected() {
        if (callbacks != null) {
            callbacks.onSettingsSelected();
        }
    }

    interface Callbacks extends ViewHolderHeader.Callbacks, ViewHolderTag.Callbacks, ViewHolderFooter.Callbacks {

    }

}
