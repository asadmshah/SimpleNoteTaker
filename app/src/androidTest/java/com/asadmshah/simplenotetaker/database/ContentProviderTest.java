package com.asadmshah.simplenotetaker.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.asadmshah.simplenotetaker.database.DatabaseContract.NoteTags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Notes;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Tags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.TagsOfNote;

public class ContentProviderTest extends ProviderTestCase2<DatabaseProvider> {

    private ContentResolver resolver;

    public ContentProviderTest() {
        super(DatabaseProvider.class, DatabaseContract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = getMockContentResolver();

        insertNote("A1", "B1", 1);
        insertNote("A2", "B2", 2);
        insertNote("A3", "B3", 3);
        insertTag("A", 1);
        insertTag("B", 2);
        insertTag("C", 3);
        insertNoteTag(1, 1);
        insertNoteTag(1, 2);
        insertNoteTag(1, 3);
        insertNoteTag(2, 2);
        insertNoteTag(2, 3);
    }

    @Override
    protected void tearDown() throws Exception {

    }

    public void test_QueryNotesList() {
        Cursor cursor = resolver.query(Notes.buildUri(), null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToNext());
        assertEquals(1, cursor.getLong(0));
        assertTrue(cursor.moveToNext());
        assertEquals(2, cursor.getLong(0));
        assertTrue(cursor.moveToNext());
        assertEquals(3, cursor.getLong(0));
        cursor.close();
    }

    public void test_QueryNotesItem() {
        Cursor cursor = resolver.query(Notes.buildUri(2), null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToNext());
        assertEquals(2, cursor.getLong(0));
        cursor.close();
    }

    public void test_QueryTagsList() {
        Cursor cursor = resolver.query(Tags.buildUri(), null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToNext());
        assertEquals(1, cursor.getLong(0));
        assertTrue(cursor.moveToNext());
        assertEquals(2, cursor.getLong(0));
        assertTrue(cursor.moveToNext());
        assertEquals(3, cursor.getLong(0));
        cursor.close();
    }

    public void test_QueryTagsOfNoteList() {
        Cursor cursor = resolver.query(TagsOfNote.buildUri(2), null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToNext());
        assertEquals(2, cursor.getLong(0));
        assertTrue(cursor.moveToNext());
        assertEquals(2, cursor.getLong(0));
        cursor.close();
    }

    public void test_QueryNotesOfTagList() {
        Cursor cursor = resolver.query(DatabaseContract.NotesOfTag.buildUri(2), null, null, null, null);
        assertNotNull(cursor);
        assertTrue(cursor.moveToNext());
        assertEquals(1, cursor.getLong(0));
        assertTrue(cursor.moveToNext());
        assertEquals(2, cursor.getLong(0));
        cursor.close();
    }

    public void test_InsertNote() {
        Uri uri = insertNote("Title", "Text", System.currentTimeMillis());
        assertEquals(4, ContentUris.parseId(uri));
    }

    public void test_InsertTag() {
        Uri uri = insertTag("Tag", Color.YELLOW);
        assertEquals(4, ContentUris.parseId(uri));
    }

    public void test_InsertNoteTag() {
        Uri uri = insertNoteTag(3, 3);
        assertEquals(6, ContentUris.parseId(uri));
    }

    public void test_DeleteNote() {
        assertEquals(1, resolver.delete(Notes.buildUri(2), null, null));
        assertEquals(0, resolver.delete(Notes.buildUri(5), null, null));
    }

    public void test_DeleteTag() {
        assertEquals(1, resolver.delete(Tags.buildUri(1), null, null));
        assertEquals(0, resolver.delete(Tags.buildUri(5), null, null));
    }

    public void test_DeleteNoteTag() {
        String selection = NoteTags.NOTE_ID + " = ? AND " + NoteTags.TAG_ID + " = ?";
        assertEquals(1, resolver.delete(NoteTags.buildUri(), selection, new String[]{"2", "2"}));
        assertEquals(0, resolver.delete(NoteTags.buildUri(), selection, new String[]{"2", "1"}));
    }

    public void test_UpdateNote() {
        ContentValues cv = new ContentValues();
        cv.put(Notes.TITLE, "Updated A1");
        assertEquals(1, resolver.update(Notes.buildUri(1), cv, null, null));
        assertEquals(0, resolver.update(Notes.buildUri(5), cv, null, null));
    }

    public void test_UpdateTag() {
        ContentValues cv = new ContentValues();
        cv.put(Tags.LABEL, "Updated A");
        assertEquals(1, resolver.update(Tags.buildUri(1), cv, null, null));
        assertEquals(0, resolver.update(Tags.buildUri(5), cv, null, null));
    }

    public void test_BulkInsertTags() {
        ContentValues[] cvs = new ContentValues[3];
        cvs[0] = new ContentValues();
        cvs[0].put(NoteTags.NOTE_ID, 3);
        cvs[0].put(NoteTags.TAG_ID, 1);
        cvs[1] = new ContentValues();
        cvs[1].put(NoteTags.NOTE_ID, 3);
        cvs[1].put(NoteTags.TAG_ID, 2);
        cvs[2] = new ContentValues();
        cvs[2].put(NoteTags.NOTE_ID, 3);
        cvs[2].put(NoteTags.TAG_ID, 3);
        assertEquals(3, resolver.bulkInsert(NoteTags.buildUri(), cvs));
    }

    private Uri insertNote(String title, String text, long createdOn) {
        ContentValues cv = new ContentValues();
        cv.put(Notes.TITLE, title);
        cv.put(Notes.TEXT, text);
        cv.put(Notes.CREATED_ON, createdOn);
        return resolver.insert(Notes.buildUri(), cv);
    }

    private Uri insertTag(String label, int color) {
        ContentValues cv = new ContentValues();
        cv.put(Tags.LABEL, label);
        cv.put(Tags.COLOR, color);
        return resolver.insert(Tags.buildUri(), cv);
    }

    private Uri insertNoteTag(long noteId, long tagId) {
        ContentValues cv = new ContentValues();
        cv.put(NoteTags.NOTE_ID, noteId);
        cv.put(NoteTags.TAG_ID, tagId);
        return resolver.insert(NoteTags.buildUri(), cv);
    }

}
