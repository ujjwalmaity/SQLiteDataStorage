package dev.ujjwal.sqlitedatastorage.data;

import android.provider.BaseColumns;

public final class StudentContract {

    private StudentContract() {
    }

    public static final class StudentEntry implements BaseColumns {

        public final static String TABLE_NAME = "students";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_QR_CODE_ID = "qr";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_BATCH = "batch";
    }
}