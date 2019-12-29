package dev.ujjwal.sqlitedatastorage.data;

import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class StudentDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "studentAttendance.db";
    private static final int DATABASE_VERSION = 1;

    public StudentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE " + StudentEntry.TABLE_NAME + " ("
                + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StudentEntry.COLUMN_QR_CODE_ID + " TEXT NOT NULL, "
                + StudentEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + StudentEntry.COLUMN_BATCH + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StudentEntry.TABLE_NAME);
        onCreate(db);
    }


    public boolean create(String qr, String name, String batch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentEntry.COLUMN_QR_CODE_ID, qr);
        contentValues.put(StudentEntry.COLUMN_NAME, name);
        contentValues.put(StudentEntry.COLUMN_BATCH, batch);
        long result = db.insert(StudentEntry.TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor read() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + StudentEntry.TABLE_NAME, null);
        return cursor;
    }

    public boolean update(String id, String batch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(StudentEntry.COLUMN_BATCH, batch);
        int result = db.update(StudentEntry.TABLE_NAME, contentValues, "_ID = ?", new String[]{id});
        if (result > 0)
            return true;
        else
            return false;
    }

    public boolean delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(StudentEntry.TABLE_NAME, "_ID = ?", new String[]{id});
        if (result > 0)
            return true;
        else
            return false;
    }
}
