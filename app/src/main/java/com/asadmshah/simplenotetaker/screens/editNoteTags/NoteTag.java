package com.asadmshah.simplenotetaker.screens.editNoteTags;

import com.asadmshah.simplenotetaker.models.Tag;

import auto.parcel.AutoParcel;

@AutoParcel
abstract class NoteTag {

    public abstract Tag getTag();
    public abstract boolean isTagged();

    static NoteTag create(Tag tag, boolean isTagged) {
        return new AutoParcel_NoteTag(tag, isTagged);
    }

}

