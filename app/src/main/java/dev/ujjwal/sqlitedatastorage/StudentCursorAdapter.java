package dev.ujjwal.sqlitedatastorage;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class StudentCursorAdapter extends CursorAdapter {

    public StudentCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_show, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.item_show_name);
        TextView batchTextView = view.findViewById(R.id.item_show_batch);

        int nameColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_NAME);
        int batchColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_BATCH);

        String studentName = cursor.getString(nameColumnIndex);
        String studentBatch = cursor.getString(batchColumnIndex);

        nameTextView.setText(studentName);
        batchTextView.setText(studentBatch);
    }
}
