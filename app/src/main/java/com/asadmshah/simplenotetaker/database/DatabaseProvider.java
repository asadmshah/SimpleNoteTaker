package com.asadmshah.simplenotetaker.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.asadmshah.simplenotetaker.database.DatabaseContract.NoteTags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Notes;
import com.asadmshah.simplenotetaker.database.DatabaseContract.NotesOfTag;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Tags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.TagsOfNote;

import timber.log.Timber;

import static com.asadmshah.simplenotetaker.database.DatabaseContract.AUTHORITY;

public class DatabaseProvider extends ContentProvider {

    private static final int NOTES_LIST = 10;
    private static final int NOTES_ITEM = 11;
    private static final int TAGS_LIST = 20;
    private static final int TAGS_ITEM = 21;
    private static final int NOTE_TAGS_LIST = 30;
    private static final int NOTE_TAGS_ITEM = 31;
    private static final int TAGS_OF_NOTE_LIST = 40;
    private static final int NOTES_OF_TAG_LIST = 50;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(AUTHORITY, Notes.PATH, NOTES_LIST);
        URI_MATCHER.addURI(AUTHORITY, Notes.PATH + "/#", NOTES_ITEM);
        URI_MATCHER.addURI(AUTHORITY, Tags.PATH, TAGS_LIST);
        URI_MATCHER.addURI(AUTHORITY, Tags.PATH + "/#", TAGS_ITEM);
        URI_MATCHER.addURI(AUTHORITY, NoteTags.PATH, NOTE_TAGS_LIST);
        URI_MATCHER.addURI(AUTHORITY, NoteTags.PATH + "/#", NOTE_TAGS_ITEM);
        URI_MATCHER.addURI(AUTHORITY, TagsOfNote.PATH + "/#", TAGS_OF_NOTE_LIST);
        URI_MATCHER.addURI(AUTHORITY, NotesOfTag.PATH + "/#", NOTES_OF_TAG_LIST);
    }

    private SQLiteOpenHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case NOTES_LIST:
                return Notes.CONTENT_TYPE;
            case NOTES_ITEM:
                return Notes.CONTENT_ITEM_TYPE;
            case TAGS_LIST:
                return Tags.CONTENT_TYPE;
            case TAGS_ITEM:
                return Tags.CONTENT_ITEM_TYPE;
            case NOTE_TAGS_LIST:
                return NoteTags.CONTENT_TYPE;
            case NOTE_TAGS_ITEM:
                return NoteTags.CONTENT_ITEM_TYPE;
            case TAGS_OF_NOTE_LIST:
                return TagsOfNote.CONTENT_TYPE;
            case NOTES_OF_TAG_LIST:
                return NotesOfTag.CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case NOTES_LIST:
                return db.query(Notes.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case NOTES_ITEM:
                selection = Notes._ID + " = ? ";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.query(Notes.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case TAGS_LIST:
                return db.query(Tags.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case TAGS_OF_NOTE_LIST:
                selection = TagsOfNote.NOTE_ID + " = ? ";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.query(TagsOfNote.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            case NOTES_OF_TAG_LIST:
                selection = NotesOfTag.TAG_ID + " = ? ";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.query(NotesOfTag.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long resultId;
        Uri resultUri;
        switch (URI_MATCHER.match(uri)) {
            case NOTES_LIST:
                resultId = db.insert(Notes.TABLE_NAME, null, values);
                resultUri = Notes.CONTENT_URI;
                break;
            case TAGS_LIST:
                resultId = db.insert(Tags.TABLE_NAME, null, values);
                resultUri = Tags.CONTENT_URI;
                break;
            case NOTE_TAGS_LIST:
                resultId = db.insert(NoteTags.TABLE_NAME, null, values);
                resultUri = NoteTags.CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (resultId <= 0) {
            throw new android.database.SQLException("Can't insert with uri: " + uri);
        }
        notifyChange(uri);
        return ContentUris.withAppendedId(resultUri, resultId);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int deletedCount;
        switch (URI_MATCHER.match(uri)) {
            case NOTES_ITEM:
                deletedCount = deleteItem(db, Notes.TABLE_NAME, uri);
                break;
            case TAGS_ITEM:
                deletedCount = deleteItem(db, Tags.TABLE_NAME, uri);
                break;
            case NOTE_TAGS_LIST:
                deletedCount = db.delete(NoteTags.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (deletedCount > 0) {
            notifyChange(uri);
        }
        return deletedCount;
    }

    private int deleteItem(SQLiteDatabase db, String table, Uri uri) {
        return db.delete(table, BaseColumns._ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int updatedCount;
        Uri additionalUri;
        switch (URI_MATCHER.match(uri)) {
            case NOTES_ITEM:
                updatedCount = updateItem(db, Notes.TABLE_NAME, values, uri);
                additionalUri = NotesOfTag.buildUri();
                break;
            case TAGS_ITEM:
                updatedCount = updateItem(db, Tags.TABLE_NAME, values, uri);
                additionalUri = TagsOfNote.buildUri();
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (updatedCount > 0) {
            notifyChange(uri);
            notifyChange(additionalUri);
        }
        return updatedCount;
    }

    private int updateItem(SQLiteDatabase db, String table, ContentValues cv, Uri uri) {
        return db.update(table, cv, BaseColumns._ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)) {
            case NOTE_TAGS_LIST:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues cv : values) {
                        if (db.insert(NoteTags.TABLE_NAME, null, cv) > 0) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (returnCount > 0) {
                    notifyChange(uri);
                }
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private void notifyChange(Uri uri) {
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Timber.e(e, "Can't notify change on uri");
        }
    }

}
