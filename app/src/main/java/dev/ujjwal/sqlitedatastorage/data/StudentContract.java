package dev.ujjwal.sqlitedatastorage.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class StudentContract {

    public static final String CONTENT_AUTHORITY = "dev.ujjwal.sqlitedatastorage";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STUDENTS = "students";

    private StudentContract() {
    }

    public static final class StudentEntry implements BaseColumns {

        public final static String TABLE_NAME = "students";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_QR_CODE_ID = "qr";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_BATCH = "batch";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STUDENTS);
    }
}