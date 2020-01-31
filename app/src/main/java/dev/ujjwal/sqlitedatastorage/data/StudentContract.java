package dev.ujjwal.sqlitedatastorage.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentResolver;

import java.util.Date;
import java.text.SimpleDateFormat;

public final class StudentContract {

    public static final String CONTENT_AUTHORITY = "dev.ujjwal.sqlitedatastorage";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STUDENTS = "students";

    private StudentContract() {
    }

    public static final class StudentEntry implements BaseColumns {

        public final static String TABLE_NAME = "students";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_BATCH = "batch";
        public final static String COLUMN_TIMESTAMP = new SimpleDateFormat("_dd_MM_yyyy").format(new Date());

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STUDENTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STUDENTS;
    }
}