package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import com.asadmshah.simplenotetaker.database.Database;
import com.asadmshah.simplenotetaker.models.Tag;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NavigationDrawerPresenter implements NavigationDrawerContract.Presenter {

    private final NavigationDrawerContract.View view;
    private final Database database;
    private final NavigationDrawerContract.Parent parent;
    private final DataSource dataSource = new DataSource() {
        @Override
        public Tag getTag(int position) {
            return tagsList.get(position);
        }

        @Override
        public int getCount() {
            return tagsList != null ? tagsList.size() : 0;
        }
    };


    private List<Tag> tagsList;
    private Subscription tagsSubscription;

    public NavigationDrawerPresenter(NavigationDrawerContract.View view, NavigationDrawerContract.Parent parent, Database database) {
        this.view = view;
        this.parent = parent;
        this.database = database;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void reloadTags() {
        unSubscribeFromUpdates();
        view.setTagsStateLoading(true);
        tagsSubscription = database.getTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tags -> {
                    tagsList = tags;
                    view.tagsLoaded();
                    view.setTagsStateLoading(false);
                });
    }

    @Override
    public void onAllNotesSelected() {
        parent.onAllNotesSelected();
        parent.closeDrawer();
    }

    @Override
    public void onEditTagsSelected() {
        parent.onEditTagsSelected();
        parent.closeDrawer();
    }

    @Override
    public void onTagSelected(long tagId) {
        parent.onTagSelected(tagId);
        parent.closeDrawer();
    }

    @Override
    public void onCreateTagSelected() {
        parent.onCreateTagSelected();
        parent.closeDrawer();
    }

    @Override
    public void onSettingsSelected() {
        parent.onSettingsSelected();
        parent.closeDrawer();
    }

    @Override
    public void unSubscribeFromUpdates() {
        if (tagsSubscription != null && !tagsSubscription.isUnsubscribed()) {
            tagsSubscription.unsubscribe();
        }
    }

}
