package com.asadmshah.simplenotetaker.screens.notesList;

import com.asadmshah.simplenotetaker.models.Note;

interface DataSource {

    Note getNote(int position);

    int getCount();
}
