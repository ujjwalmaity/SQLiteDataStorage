package dev.ujjwal.sqlitedatastorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Date;
import java.text.SimpleDateFormat;

import dev.ujjwal.sqlitedatastorage.data.StudentDbHelper;
import dev.ujjwal.sqlitedatastorage.recycler.AttendanceAdapter;
import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class AttendanceActivity extends AppCompatActivity {

    Spinner spinner;
    String[] section;
    String selectedSection;

    RecyclerView recyclerView;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        init();

        readSection();
    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.recycler_view);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void readSection() {
        StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());
        SQLiteDatabase db = studentDbHelper.getReadableDatabase();
        String[] projection = {StudentEntry.COLUMN_BATCH};
        Cursor cursor = db.query(StudentEntry.TABLE_NAME, projection,
                null, null,
                StudentEntry.COLUMN_BATCH, null, null);
        if (cursor.getCount() == 0) {
            return;
        }
        section = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            section[i++] = cursor.getString(0);
        }
        cursor.close();

        setSpinner();
    }

    private void setSpinner() {
        ArrayAdapter aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, section);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selectedSection = spinner.getSelectedItem().toString();
                showAttendance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinner.setVisibility(View.VISIBLE);
    }

    private void showAttendance() {
        StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());
        SQLiteDatabase db = studentDbHelper.getReadableDatabase();

        String date = preferences.getString("date", "");
        String timeStamp = new SimpleDateFormat("_dd_MM_yyyy").format(new Date());
        if (!date.equals(timeStamp)) {
            db.execSQL("ALTER TABLE " + StudentEntry.TABLE_NAME + " ADD COLUMN " + timeStamp + " INTEGER DEFAULT 0");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("date", timeStamp);
            editor.apply();
        }

        String[] projection = {
                StudentEntry._ID,
                StudentEntry.COLUMN_NAME,
                StudentEntry.COLUMN_BATCH,
                timeStamp};
        String selection = StudentEntry.COLUMN_BATCH + "=?";
        String[] selectionArgs = {selectedSection};
        Cursor cursor = db.query(StudentEntry.TABLE_NAME, projection,
                selection, selectionArgs,
                null, null, null);
        if (cursor.getCount() == 0) {
            return;
        }
        Integer[] id = new Integer[cursor.getCount()];
        String[] name = new String[cursor.getCount()];
        Integer[] attendance = new Integer[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            id[i] = cursor.getInt(0);
            name[i] = cursor.getString(1);
            attendance[i++] = cursor.getInt(3);
        }
        cursor.close();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(this, id, name, attendance);
        recyclerView.setAdapter(attendanceAdapter);
    }

    public static void makeAttendance(Context context, Integer id, Integer attendance) {
        String timeStamp = new SimpleDateFormat("_dd_MM_yyyy").format(new Date());

        StudentDbHelper studentDbHelper = new StudentDbHelper(context);
        SQLiteDatabase db = studentDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(timeStamp, attendance);
        db.update(StudentEntry.TABLE_NAME, contentValues, "_ID = ?", new String[]{id.toString()});
    }
}
