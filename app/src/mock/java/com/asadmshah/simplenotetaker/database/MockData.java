package com.asadmshah.simplenotetaker.database;

import android.graphics.Color;

import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.models.Tag;

import java.util.ArrayList;
import java.util.List;

public interface MockData {

    List<Note> insertedNotes = new ArrayList<>();
    List<Tag> insertedTags = new ArrayList<>();

    interface Note1 {
        String TITLE = "Note1.Title";
        String TEXT = "Note1.Text";
    }

    interface Note2 {
        String TITLE = "Note2.Title";
        String TEXT = "Note2.Text";
    }

    interface Tag1 {
        String LABEL = "Tag1";
        int COLOR = Color.RED;
    }

    interface Tag2 {
        String LABEL = "Tag2";
        int COLOR = Color.GREEN;
    }

    interface Tag3 {
        String LABEL = "Tag3";
        int COLOR = Color.BLUE;
    }

}
