package com.asadmshah.simplenotetaker.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String AUTHORITY = "com.asadmshah.simplenotetaker.database";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final String BASE_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY;
    public static final String BASE_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY;

    public static final class Notes implements BaseColumns {

        public static final String TABLE_NAME = "Notes";

        public static final String TITLE = "title";
        public static final String TEXT = "text";
        public static final String CREATED_ON = "created_on";

        public static final String PATH = TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + "/" + PATH;

        public static Uri buildUri() {
            return CONTENT_URI;
        }

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class Tags implements BaseColumns {

        public static final String TABLE_NAME = "Tags";

        public static final String LABEL = "label";
        public static final String COLOR = "color";

        public static final String PATH = TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + "/" + PATH;

        public static Uri buildUri() {
            return CONTENT_URI;
        }

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class NoteTags implements BaseColumns {

        public static final String TABLE_NAME = "NoteTags";

        public static final String NOTE_ID = "note_id";
        public static final String TAG_ID = "tag_id";

        public static final String PATH = TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + "/" + PATH;
        public static final String CONTENT_ITEM_TYPE = BASE_CONTENT_ITEM_TYPE + "/" + PATH;

        public static Uri buildUri() {
            return CONTENT_URI;
        }

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class TagsOfNote {
        public static  final String TABLE_NAME = "TagsOfNote";

        public static final String NOTE_ID = NoteTags.NOTE_ID;
        public static final String TAG_ID = NoteTags.TAG_ID;
        public static final String LABEL = Tags.LABEL;
        public static final String COLOR = Tags.COLOR;

        public static final String PATH = NoteTags.TABLE_NAME + "/" + TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + "/" + PATH;

        public static Uri buildUri() {
            return CONTENT_URI;
        }

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class NotesOfTag {
        public static final String TABLE_NAME = "NotesOfTag";

        public static final String NOTE_ID = NoteTags.NOTE_ID;
        public static final String TAG_ID = NoteTags.TAG_ID;
        public static final String TITLE = Notes.TITLE;
        public static final String TEXT = Notes.TEXT;
        public static final String CREATED_ON = Notes.CREATED_ON;

        public static final String PATH = NoteTags.TABLE_NAME + "/" + TABLE_NAME;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH);
        public static final String CONTENT_TYPE = BASE_CONTENT_TYPE + "/" + PATH;

        public static Uri buildUri() {
            return CONTENT_URI;
        }

        public static Uri buildUri(long tagId) {
            return ContentUris.withAppendedId(CONTENT_URI, tagId);
        }
    }

}
