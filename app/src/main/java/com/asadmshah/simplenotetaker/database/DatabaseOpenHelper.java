package com.asadmshah.simplenotetaker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.asadmshah.simplenotetaker.database.DatabaseContract.NoteTags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Notes;
import com.asadmshah.simplenotetaker.database.DatabaseContract.NotesOfTag;
import com.asadmshah.simplenotetaker.database.DatabaseContract.Tags;
import com.asadmshah.simplenotetaker.database.DatabaseContract.TagsOfNote;

class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;

    private static final String TRIGGER_ON_DELETE_NOTE = "OnDeleteNote";
    private static final String TRIGGER_ON_DELETE_TAG = "OnDeleteTag";

    public DatabaseOpenHelper(Context context) {
        super(context, Constants.NAME, null, VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Notes.TABLE_NAME + " ("
                + Notes._ID + " INTEGER PRIMARY KEY, "
                + Notes.TITLE + " TEXT, "
                + Notes.TEXT + " TEXT, "
                + Notes.CREATED_ON + " INTEGER NOT NULL );"
        );

        db.execSQL("CREATE TABLE " + Tags.TABLE_NAME + " ("
                + Tags._ID + " INTEGER PRIMARY KEY, "
                + Tags.LABEL + " TEXT NOT NULL, "
                + Tags.COLOR + " INTEGER NOT NULL );"
        );

        db.execSQL("CREATE TABLE " + NoteTags.TABLE_NAME + " ("
                + NoteTags._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + NoteTags.NOTE_ID + " INTEGER NOT NULL, "
                + NoteTags.TAG_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + NoteTags.NOTE_ID + ") REFERENCES " + Notes.TABLE_NAME + "(" + Notes._ID + "), "
                + "FOREIGN KEY (" + NoteTags.TAG_ID + ") REFERENCES " + Tags.TABLE_NAME + "(" + Tags._ID + "), "
                + "UNIQUE(" + NoteTags.NOTE_ID + ", " + NoteTags.TAG_ID + ") ON CONFLICT FAIL );"
        );

        db.execSQL("CREATE VIEW " + TagsOfNote.TABLE_NAME + " AS SELECT "
                + NoteTags.TABLE_NAME + "." + TagsOfNote.NOTE_ID + ", "
                + NoteTags.TABLE_NAME + "." + TagsOfNote.TAG_ID + ", "
                + Tags.TABLE_NAME + "." + TagsOfNote.LABEL + ", "
                + Tags.TABLE_NAME + "." + TagsOfNote.COLOR
                + " FROM " + NoteTags.TABLE_NAME + " INNER JOIN " + Tags.TABLE_NAME
                + " WHERE " + NoteTags.TABLE_NAME + "." + NoteTags.TAG_ID + "=" + Tags.TABLE_NAME + "." + Tags._ID + ";"
        );

        db.execSQL("CREATE VIEW " + NotesOfTag.TABLE_NAME + " AS SELECT "
                + NoteTags.TABLE_NAME + "." + NotesOfTag.NOTE_ID + ", "
                + NoteTags.TABLE_NAME + "." + NotesOfTag.TAG_ID + ", "
                + Notes.TABLE_NAME + "." + NotesOfTag.TITLE + ", "
                + Notes.TABLE_NAME + "." + NotesOfTag.TEXT + ", "
                + Notes.TABLE_NAME + "." + NotesOfTag.CREATED_ON
                + " FROM " + NoteTags.TABLE_NAME + " INNER JOIN " + Notes.TABLE_NAME
                + " WHERE " + NoteTags.TABLE_NAME + "." + NoteTags.NOTE_ID + "=" + Notes.TABLE_NAME + "." + Notes._ID + ";"
        );

        db.execSQL("CREATE TRIGGER " + TRIGGER_ON_DELETE_NOTE + " BEFORE DELETE ON " + Notes.TABLE_NAME
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + NoteTags.TABLE_NAME + " WHERE " + NoteTags.NOTE_ID + "=OLD." + Notes._ID + ";"
                + " END;"
        );

        db.execSQL("CREATE TRIGGER " + TRIGGER_ON_DELETE_TAG + " BEFORE DELETE ON " + Tags.TABLE_NAME
                + " FOR EACH ROW BEGIN "
                + " DELETE FROM " + NoteTags.TABLE_NAME + " WHERE " + NoteTags.TAG_ID + "=OLD." + Tags._ID + ";"
                + " END;"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
