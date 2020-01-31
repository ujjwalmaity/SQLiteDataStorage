package dev.ujjwal.sqlitedatastorage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.database.Cursor;
import android.content.ContentValues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import dev.ujjwal.sqlitedatastorage.data.StudentDbHelper;
import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class MainActivity extends AppCompatActivity {

    EditText name, batch;
    EditText id, batch2;
    EditText id2;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        batch = findViewById(R.id.batch);
        id = findViewById(R.id.id);
        batch2 = findViewById(R.id.batch2);
        id2 = findViewById(R.id.id2);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isSampleDataInserted = preferences.getBoolean("isSampleDataInserted", false);
        if (!isSampleDataInserted) {
            String[] names = {"Mike", "Avi", "Will", "Nish", "Abhi", "Ved", "Neel", "Krish", "Leo", "Siddy", "Rob", "Monu", "Shau", "Rey", "Eddie", "Dave"};
            String[] batches = {"Sec A", "Group 1", "Sec B", "Batch 1", "Sec B", "Batch 1", "Sec A", "Group 1", "Batch 1", "Sec B", "Batch 1", "Sec A", "Batch 1", "Sec A", "Group 1", "Sec B"};
            int i = 0;
            for (String str : names) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(StudentEntry.COLUMN_NAME, str);
                contentValues.put(StudentEntry.COLUMN_BATCH, batches[i++]);
                getContentResolver().insert(StudentEntry.CONTENT_URI, contentValues);
            }

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isSampleDataInserted", true);
            editor.apply();
        }


        //CREATE
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(StudentEntry.COLUMN_NAME, name.getText().toString().trim());
                contentValues.put(StudentEntry.COLUMN_BATCH, batch.getText().toString().toLowerCase().trim());
                try {
                    Uri newUri = getContentResolver().insert(StudentEntry.CONTENT_URI, contentValues);

                    if (newUri == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.editor_insert_student_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.editor_insert_student_successful), Toast.LENGTH_SHORT).show();
                        name.setText("");
                        batch.setText("");
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //READ
        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] projection = {StudentEntry._ID,
                        StudentEntry.COLUMN_NAME,
                        StudentEntry.COLUMN_BATCH};

                Cursor cursor = getContentResolver().query(StudentEntry.CONTENT_URI, projection, null, null, null);

                if (cursor.getCount() == 0) {
                    showMessage("Error 404", "No Record found");
                    return;
                }

                int idColumnIndex = cursor.getColumnIndex(StudentEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_NAME);
                int batchColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_BATCH);

                StringBuffer buffer = new StringBuffer();
                while (cursor.moveToNext()) {
                    buffer.append("ID: " + cursor.getInt(idColumnIndex) + "\n");
                    buffer.append("NAME: " + cursor.getString(nameColumnIndex) + "\n");
                    buffer.append("BATCH: " + cursor.getString(batchColumnIndex) + "\n\n");
                }
                showMessage("Data", buffer.toString());

                cursor.close();
            }
        });

        //UPDATE
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(StudentEntry.COLUMN_BATCH, batch2.getText().toString().toLowerCase().trim());
                try {
                    int result = getContentResolver().update(StudentEntry.CONTENT_URI, contentValues, "_ID = ?", new String[]{id.getText().toString().trim()});

                    if (result > 0) {
                        Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_LONG).show();
                        id.setText("");
                        batch2.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Data not Update", Toast.LENGTH_LONG).show();
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //DELETE
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = getContentResolver().delete(StudentEntry.CONTENT_URI, "_ID = ?", new String[]{id2.getText().toString().trim()});

                if (result > 0) {
                    Toast.makeText(getApplicationContext(), "Data Deleted", Toast.LENGTH_LONG).show();
                    id2.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Data not Delete", Toast.LENGTH_LONG).show();
                }
            }
        });

        //MAKE ATTENDANCE
        findViewById(R.id.attendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });

        //SHOW DB
        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                startActivity(intent);
            }
        });

        //EXPORT DB TO EXTERNAL STORAGE
        findViewById(R.id.export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File data = Environment.getDataDirectory();
//                File sd = Environment.getExternalStorageDirectory();
                File sd = getExternalFilesDir("student_attendance");
                String currentDBPath = "/data/" + getPackageName() + "/databases/" + StudentDbHelper.DATABASE_NAME;
                String backupDBPath = StudentDbHelper.DATABASE_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                FileChannel source = null;
                FileChannel destination = null;
                try {
                    source = new FileInputStream(currentDB).getChannel();
                    destination = new FileOutputStream(backupDB).getChannel();
                    destination.transferFrom(source, 0, source.size());
                    source.close();
                    destination.close();
                    Toast.makeText(getApplicationContext(), "DB Exported!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "DB Not Export!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
