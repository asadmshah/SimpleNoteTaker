package com.asadmshah.simplenotetaker.screens.navigationDrawer;

public interface NavigationDrawerContract {

    interface View {

        void tagsLoaded();

        void setTagsStateLoading(boolean isLoadingState);
    }


    interface Presenter {

        DataSource getDataSource();

        void reloadTags();

        void onAllNotesSelected();

        void onEditTagsSelected();

        void onTagSelected(long tagId);

        void onCreateTagSelected();

        void onSettingsSelected();

        void unSubscribeFromUpdates();
    }

    interface Parent {

        void closeDrawer();

        void onAllNotesSelected();

        void onEditTagsSelected();

        void onTagSelected(long tagId);

        void onCreateTagSelected();

        void onSettingsSelected();
    }

}
