package com.asadmshah.simplenotetaker.database;

import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.models.Tag;

import java.util.List;

import rx.Observable;

public interface Database {

    Observable<List<Note>> getNotes();

    Observable<List<Note>> getNotesOfTag(long tagId);

    Observable<Note> getNote(long noteId);

    Observable<List<Tag>> getTags();

    Observable<List<Tag>> getTagsOfNote(long noteId);

    Note insertNote(String title, String text);

    boolean deleteNote(long noteId);

    boolean updateNote(long noteId, String title, String text);

    Tag insertTag(String label, int color);

    boolean deleteTag(long tagId);

    boolean updateTag(long tagId, String label, int color);

    boolean tagNote(long noteId, long tagId);

    boolean unTagNote(long noteId, long tagId);
}
