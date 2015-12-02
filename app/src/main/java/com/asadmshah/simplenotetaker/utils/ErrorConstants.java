package com.asadmshah.simplenotetaker.utils;

import android.content.Context;

import com.asadmshah.simplenotetaker.R;

public enum ErrorConstants {

    NOTE_DOES_NOT_EXIST(R.string.note_does_not_exist),
    TAG_DOES_NOT_EXIST(R.string.tag_does_not_exist),
    UNABLE_TO_UPDATE_NOTE(R.string.unable_to_update_note),
    UNABLE_TO_DELETE_NOTE(R.string.unable_to_delete_note),
    UNABLE_TO_TAG_NOTE(R.string.unable_to_tag_note),
    UNABLE_TO_UNTAG_NOTE(R.string.unable_to_untag_note),
    UNABLE_TO_LOAD_TAGS(R.string.unable_to_load_tags),
    NEW_TAG_REQUIRES_LABEL(R.string.new_tag_requires_label),
    NEW_TAG_REQUIRES_COLOR(R.string.new_tag_requires_color),
    UNABLE_TO_CREATE_TAG(R.string.unable_to_create_tag),
    UNABLE_TO_DELETE_TAG(R.string.unable_to_delete_tag),
    UNABLE_TO_UPDATE_TAG(R.string.unable_to_update_tag);

    private final int stringResourceId;

    ErrorConstants(int stringResourceId) {
        this.stringResourceId = stringResourceId;
    }

    public String getString(Context context) {
        return context.getString(stringResourceId);
    }
}
