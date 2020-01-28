package dev.ujjwal.sqlitedatastorage;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        String[] projection = {StudentEntry._ID,
                StudentEntry.COLUMN_QR_CODE_ID,
                StudentEntry.COLUMN_NAME,
                StudentEntry.COLUMN_BATCH};
        Cursor cursor = getContentResolver().query(
                StudentEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        ListView listView = findViewById(R.id.activity_show_listView);
        StudentCursorAdapter studentCursorAdapter = new StudentCursorAdapter(this, cursor);
        listView.setAdapter(studentCursorAdapter);
    }
}
