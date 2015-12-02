package com.asadmshah.simplenotetaker.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.asadmshah.simplenotetaker.database.DatabaseContract.NoteTags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Notes;
import com.asadmshah.simplenotetaker.database.DatabaseContract.NotesOfTag;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Tags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.TagsOfNote;
import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.models.Tag;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ProviderBackedDatabase implements Database {

    private final ContentResolver resolver;
    private final BriteContentResolver queryResolver;

    public ProviderBackedDatabase(ContentResolver resolver) {
        this.resolver = resolver;
        this.queryResolver = SqlBrite.create().wrapContentProvider(resolver);
    }

    @Override
    public Observable<List<Note>> getNotes() {
        return queryResolver.createQuery(Notes.buildUri(), NotesQuery.PROJECTION, null, null, NotesQuery.SORT, true)
                .map(query -> {
                    Cursor cursor = query.run();
                    List<Note> notes = new ArrayList<>(cursor.getCount());
                    try {
                        while (cursor.moveToNext()) {
                            notes.add(Note.create(
                                    cursor.getLong(NotesQuery.ID),
                                    cursor.getString(NotesQuery.TITLE),
                                    cursor.getString(NotesQuery.TEXT),
                                    cursor.getLong(NotesQuery.CREATED_ON)
                            ));
                        }
                    } finally {
                        cursor.close();
                    }
                    return notes;
                });
    }

    @Override
    public Observable<List<Note>> getNotesOfTag(long tagId) {
        return queryResolver.createQuery(NotesOfTag.buildUri(tagId), NotesOfTagQuery.PROJECTION, null, null, NotesOfTagQuery.SORT, true)
                .map((SqlBrite.Query query) -> {
                    Cursor cursor = query.run();
                    List<Note> notes = new ArrayList<>(cursor.getCount());
                    try {
                        while (cursor.moveToNext()) {
                            notes.add(Note.create(
                                    cursor.getLong(NotesOfTagQuery.NOTE_ID),
                                    cursor.getString(NotesOfTagQuery.TITLE),
                                    cursor.getString(NotesOfTagQuery.TEXT),
                                    cursor.getLong(NotesOfTagQuery.CREATED_ON)
                            ));
                        }
                    } finally {
                        cursor.close();
                    }
                    return notes;
                });
    }

    @Override
    public Observable<Note> getNote(long noteId) {
        return queryResolver.createQuery(Notes.buildUri(noteId), NotesQuery.PROJECTION, null, null, null, true)
                .map(query -> {
                    Cursor cursor = query.run();
                    Note note = null;
                    if (cursor.moveToNext()) {
                        note = Note.create(
                                cursor.getLong(NotesQuery.ID),
                                cursor.getString(NotesQuery.TITLE),
                                cursor.getString(NotesQuery.TEXT),
                                cursor.getLong(NotesQuery.CREATED_ON));
                    }
                    cursor.close();
                    return note;
                });
    }

    @Override
    public Observable<List<Tag>> getTags() {
        return queryResolver.createQuery(Tags.buildUri(), TagsQuery.PROJECTION, null, null, TagsQuery.SORT, true)
                .map(query -> {
                    Cursor cursor = query.run();
                    List<Tag> tags = new ArrayList<>(cursor.getCount());
                    try {
                        while (cursor.moveToNext()) {
                            tags.add(Tag.create(
                                    cursor.getLong(TagsQuery.ID),
                                    cursor.getString(TagsQuery.LABEL),
                                    cursor.getInt(TagsQuery.COLOR)
                            ));
                        }
                    } finally {
                        cursor.close();
                    }
                    return tags;
                });
    }

    @Override
    public Observable<List<Tag>> getTagsOfNote(long noteId) {
        return queryResolver.createQuery(TagsOfNote.buildUri(noteId), TagsOfNoteQuery.PROJECTION, null, null, TagsOfNoteQuery.SORT, true)
                .map(query -> {
                    Cursor cursor = query.run();
                    List<Tag> tags = new ArrayList<>(cursor.getCount());
                    try {
                        while (cursor.moveToNext()) {
                            tags.add(Tag.create(
                                    cursor.getLong(TagsOfNoteQuery.TAG_ID),
                                    cursor.getString(TagsOfNoteQuery.LABEL),
                                    cursor.getInt(TagsOfNoteQuery.COLOR)
                            ));
                        }
                    } finally {
                        cursor.close();
                    }
                    return tags;
                });
    }

    @Override
    public Note insertNote(String title, String text) {
        ContentValues cv = new ContentValues();
        cv.put(Notes.TITLE, title);
        cv.put(Notes.TEXT, text);
        cv.put(Notes.CREATED_ON, System.currentTimeMillis());
        Uri uri = resolver.insert(Notes.buildUri(), cv);
        if (uri == null) {
            return null;
        }
        return Note.create(ContentUris.parseId(uri), title, text, cv.getAsLong(Notes.CREATED_ON));
    }

    @Override
    public boolean deleteNote(long noteId) {
        return resolver.delete(Notes.buildUri(noteId), null, null) > 0;
    }

    @Override
    public boolean updateNote(long noteId, String title, String text) {
        ContentValues cv = new ContentValues();
        cv.put(Notes.TITLE, title);
        cv.put(Notes.TEXT, text);
        return resolver.update(Notes.buildUri(noteId), cv, null, null) > 0;
    }

    @Override
    public Tag insertTag(String label, int color) {
        ContentValues cv = new ContentValues();
        cv.put(Tags.LABEL, label);
        cv.put(Tags.COLOR, color);
        Uri uri = resolver.insert(Tags.buildUri(), cv);
        if (uri == null) {
            return null;
        }
        return Tag.create(ContentUris.parseId(uri), label, color);
    }

    @Override
    public boolean deleteTag(long tagId) {
        return resolver.delete(Tags.buildUri(tagId), null, null) > 0;
    }

    @Override
    public boolean updateTag(long tagId, String label, int color) {
        ContentValues cv = new ContentValues();
        cv.put(Tags.LABEL, label);
        cv.put(Tags.COLOR, color);
        return resolver.update(Tags.buildUri(tagId), cv, null, null) > 0;
    }

    @Override
    public boolean tagNote(long noteId, long tagId) {
        ContentValues cv = new ContentValues();
        cv.put(NoteTags.NOTE_ID, noteId);
        cv.put(NoteTags.TAG_ID, tagId);
        try {
            return resolver.insert(NoteTags.buildUri(), cv) != null;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean unTagNote(long noteId, long tagId) {
        String selection = NoteTags.NOTE_ID + " = ? AND " + NoteTags.TAG_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(noteId), String.valueOf(tagId)};
        return resolver.delete(NoteTags.buildUri(), selection, selectionArgs) > 0;
    }

    private interface NotesQuery {
        String[] PROJECTION = {
                Notes._ID,
                Notes.TITLE,
                Notes.TEXT,
                Notes.CREATED_ON
        };

        int ID = 0;
        int TITLE = 1;
        int TEXT = 2;
        int CREATED_ON = 3;

        String SORT = Notes.CREATED_ON + " DESC";
    }

    private interface TagsQuery {
        String[] PROJECTION = {
                Tags._ID,
                Tags.LABEL,
                Tags.COLOR
        };

        int ID = 0;
        int LABEL = 1;
        int COLOR = 2;

        String SORT = Tags.LABEL + " ASC";
    }

    private interface TagsOfNoteQuery {
        String[] PROJECTION = {
                TagsOfNote.TAG_ID,
                TagsOfNote.LABEL,
                TagsOfNote.COLOR
        };

        int TAG_ID = 0;
        int LABEL = 1;
        int COLOR = 2;

        String SORT = TagsOfNote.LABEL + " ASC";
    }

    private interface NotesOfTagQuery {
        String[] PROJECTION = {
                NotesOfTag.NOTE_ID,
                NotesOfTag.TITLE,
                NotesOfTag.TEXT,
                NotesOfTag.CREATED_ON
        };

        int NOTE_ID = 0;
        int TITLE = 1;
        int TEXT = 2;
        int CREATED_ON = 3;

        String SORT = NotesOfTag.CREATED_ON + " DESC";
    }

}
