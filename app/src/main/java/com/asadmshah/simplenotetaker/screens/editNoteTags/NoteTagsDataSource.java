package com.asadmshah.simplenotetaker.screens.editNoteTags;

interface NoteTagsDataSource {

    NoteTag getNoteTag(int position);

    int getCount();
}
