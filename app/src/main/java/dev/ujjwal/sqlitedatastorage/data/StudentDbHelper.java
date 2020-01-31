package dev.ujjwal.sqlitedatastorage.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class StudentDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StudentAttendance.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + StudentEntry.TABLE_NAME + " ("
            + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + StudentEntry.COLUMN_NAME + " TEXT NOT NULL, "
            + StudentEntry.COLUMN_BATCH + " TEXT NOT NULL);";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + StudentEntry.TABLE_NAME;

    public StudentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
