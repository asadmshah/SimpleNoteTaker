package com.asadmshah.simplenotetaker.database;

import android.test.ProviderTestCase2;

import com.asadmshah.simplenotetaker.models.Note;
import com.asadmshah.simplenotetaker.models.Tag;

import java.util.List;

public class ProviderBackedDatabaseTest extends ProviderTestCase2<DatabaseProvider> {

    private Database database;

    public ProviderBackedDatabaseTest() {
        super(DatabaseProvider.class, DatabaseContract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        database = new ProviderBackedDatabase(getMockContentResolver());
        database.insertNote("A1", "B1");
        database.insertTag("A", 1);
        database.insertNote("A2", "B2");
        database.insertTag("B", 2);
        database.insertTag("C", 3);

        database.tagNote(1, 1);
        database.tagNote(1, 2);
        database.tagNote(1, 3);
        database.tagNote(2, 2);
        database.tagNote(2, 3);

        database.insertNote("A3", "B3");
    }

    public void testGetNotes() {
        List<Note> notes = database.getNotes().toBlocking().first();
        assertNotNull(notes);
        assertEquals(3, notes.size());
        assertEquals("A3", notes.get(0).title());
        assertEquals("B1", notes.get(2).text());
    }

    public void testGetNote() {
        Note note;

        note = database.getNote(2).toBlocking().first();
        assertNotNull(note);
        assertEquals("A2", note.title());
        assertEquals("B2", note.text());

        note = database.getNote(4).toBlocking().first();
        assertNull(note);
    }

    public void testGetTags() {
        List<Tag> tags = database.getTags().toBlocking().first();
        assertNotNull(tags);
        assertEquals(3, tags.size());
        assertEquals("A", tags.get(0).label());
        assertEquals("C", tags.get(2).label());
    }

    public void testGetTagsOfNote() {
        List<Tag> tags;
        tags = database.getTagsOfNote(1).toBlocking().first();
        assertNotNull(tags);
        assertEquals(3, tags.size());
        assertEquals("A", tags.get(0).label());
        assertEquals("C", tags.get(2).label());

        tags = database.getTagsOfNote(2).toBlocking().first();
        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("B", tags.get(0).label());
        assertEquals("C", tags.get(1).label());

        tags = database.getTagsOfNote(3).toBlocking().first();
        assertNotNull(tags);
        assertEquals(0, tags.size());
    }

    public void testGetNotesOfTag() {
        List<Note> notes;
        notes = database.getNotesOfTag(2).toBlocking().first();
        assertNotNull(notes);
        assertEquals(2, notes.size());
        assertEquals("A2", notes.get(0).title());
        assertEquals("A1", notes.get(1).title());

        notes = database.getNotesOfTag(1).toBlocking().first();
        assertNotNull(notes);
        assertEquals(1, notes.size());
        assertEquals("A1", notes.get(0).title());
    }

    public void testInsertNote() {
        Note note = database.insertNote("A4", "B4");
        assertNotNull(note);
        assertEquals("A4", note.title());
        assertEquals("B4", note.text());
    }

    public void testDeleteNote() {
        assertTrue(database.deleteNote(1));
        assertFalse(database.deleteNote(5));
    }

    public void testUpdateNote() {
        assertTrue(database.updateNote(1, "Updated A1", "Updated B1"));
        assertFalse(database.updateNote(5, "Updated A5", "Updated B5"));
    }

    public void testInsertTag() {
        Tag tag = database.insertTag("D", 4);
        assertNotNull(tag);
        assertEquals("D", tag.label());
        assertEquals(4, tag.id());
    }

    public void testDeleteTag() {
        assertTrue(database.deleteTag(1));
        assertFalse(database.deleteTag(5));
    }

    public void testUpdateTag() {
        assertTrue(database.updateTag(1, "Updated A", 1));
        assertFalse(database.updateTag(5, "Updated E", 5));
    }

    public void testTagNote() {
        assertTrue(database.tagNote(3, 1));
        assertFalse(database.tagNote(1, 1));
        assertFalse(database.tagNote(5, 1));
        assertFalse(database.tagNote(1, 5));
    }

    public void testUntagNote() {
        assertTrue(database.unTagNote(1, 1));
        assertFalse(database.unTagNote(5, 5));
        assertFalse(database.unTagNote(2, 1));
    }

}
