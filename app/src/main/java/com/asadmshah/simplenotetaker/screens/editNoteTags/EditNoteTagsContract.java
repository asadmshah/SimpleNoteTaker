package com.asadmshah.simplenotetaker.screens.editNoteTags;

import com.asadmshah.simplenotetaker.utils.ErrorConstants;

public interface EditNoteTagsContract {

    interface View {

        void tagsDataSetChanged();

        void tagAdded(int position);

        void tagChanged(int position);

        void setTagsLoadingState(boolean isLoading);

        void showTagCreateButton(boolean show);

        void selectTagColor(int color);

        void clearNewTagLabel();

        void clearNewTagColor();

        void showError(ErrorConstants error);
    }

    interface Presenter {

        NoteTagsDataSource getNoteTagsDataSource();

        void reloadTags();

        void onToggleTag(int position);

        void onCreateTagLabelChanged(String newText);

        void onCreateTagColorChanged(int color);

        void onCreateTag();

        void onBackPressed();
    }

    interface Parent {

        void closeEditNoteTagsScreen();
    }

}
