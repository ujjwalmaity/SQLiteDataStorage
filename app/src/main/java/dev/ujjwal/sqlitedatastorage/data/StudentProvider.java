package dev.ujjwal.sqlitedatastorage.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class StudentProvider extends ContentProvider {

    private StudentDbHelper studentDbHelper;

    private static final int STUDENTS = 100;
    private static final int STUDENTS_ID = 101;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(StudentContract.CONTENT_AUTHORITY, StudentContract.PATH_STUDENTS, STUDENTS);
        URI_MATCHER.addURI(StudentContract.CONTENT_AUTHORITY, StudentContract.PATH_STUDENTS + "/#", STUDENTS_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        studentDbHelper = new StudentDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = studentDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = URI_MATCHER.match(uri);

        switch (match) {
            case STUDENTS:
                cursor = sqLiteDatabase.query(StudentEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STUDENTS_ID:
                selection = StudentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(StudentEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Can not query unknown URI " + uri);
        }

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String name = values.getAsString(StudentEntry.COLUMN_NAME);
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Student requires a name");
        }

        SQLiteDatabase sqLiteDatabase = studentDbHelper.getWritableDatabase();

        Uri newUri;

        int match = URI_MATCHER.match(uri);

        switch (match) {
            case STUDENTS:
                long id = sqLiteDatabase.insert(StudentEntry.TABLE_NAME, null, values);
                if (id == -1) {
                    return null;
                }
                newUri = ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        return newUri;
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (values.containsKey(StudentEntry.COLUMN_BATCH)) {
            String batch = values.getAsString(StudentEntry.COLUMN_BATCH);
            if (batch == null || batch.equals("")) {
                throw new IllegalArgumentException("Student requires batch");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = studentDbHelper.getWritableDatabase();

        int result;

        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case STUDENTS:
                result = sqLiteDatabase.update(StudentEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case STUDENTS_ID:
                selection = StudentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result = sqLiteDatabase.update(StudentEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

        return result;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = studentDbHelper.getWritableDatabase();

        int result;

        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case STUDENTS:
                result = sqLiteDatabase.delete(StudentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENTS_ID:
                selection = StudentEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result = sqLiteDatabase.delete(StudentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        return result;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = URI_MATCHER.match(uri);

        switch (match) {
            case STUDENTS:
                return StudentEntry.CONTENT_LIST_TYPE;
            case STUDENTS_ID:
                return StudentEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
