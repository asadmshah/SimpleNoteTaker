package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asadmshah.simplenotetaker.R;

import javax.inject.Inject;

public class NavigationDrawerFragment extends Fragment implements NavigationDrawerContract.View,
        Adapter.Callbacks {

    public static final String TAG = NavigationDrawerFragment.class.getSimpleName();

    @Inject NavigationDrawerContract.Presenter presenter;

    private Adapter adapter;
    private RecyclerView viewList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new Adapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewList = (RecyclerView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        viewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        viewList.setAdapter(adapter);
        return viewList;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.setDataSource(presenter.getDataSource());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.reloadTags();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribeFromUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.setDataSource(null);
    }

    @Override
    public void tagsLoaded() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTagsStateLoading(boolean isLoadingState) {
        adapter.setCallbacks(isLoadingState ? null : this);
    }

    @Override
    public void onAllNotesSelected() {
        presenter.onAllNotesSelected();
    }

    @Override
    public void onEditTagsSelected() {
        presenter.onEditTagsSelected();
    }

    @Override
    public void onTagSelected(ViewHolderTag viewHolder) {
        presenter.onTagSelected(viewHolder.getItemId());
    }

    @Override
    public void onCreateNewTagSelected() {
        presenter.onCreateTagSelected();
    }

    @Override
    public void onSettingsSelected() {
        presenter.onSettingsSelected();
    }
}
