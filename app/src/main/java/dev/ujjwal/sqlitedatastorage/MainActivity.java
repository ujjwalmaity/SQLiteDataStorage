package dev.ujjwal.sqlitedatastorage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import dev.ujjwal.sqlitedatastorage.data.StudentDbHelper;
import dev.ujjwal.sqlitedatastorage.data.StudentContract.StudentEntry;

public class MainActivity extends AppCompatActivity {

    StudentDbHelper studentDbHelper;

    EditText qr, name, batch;
    EditText id, batch2;
    EditText id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentDbHelper = new StudentDbHelper(getApplicationContext());

        qr = findViewById(R.id.qr);
        name = findViewById(R.id.name);
        batch = findViewById(R.id.batch);
        id = findViewById(R.id.id);
        batch2 = findViewById(R.id.batch2);
        id2 = findViewById(R.id.id2);


        //CREATE
        findViewById(R.id.create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qr.getText().toString().trim().equals("") || name.getText().toString().trim().equals("") || batch.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());

                SQLiteDatabase db = studentDbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(StudentEntry.COLUMN_QR_CODE_ID, qr.getText().toString().trim());
                contentValues.put(StudentEntry.COLUMN_NAME, name.getText().toString().trim());
                contentValues.put(StudentEntry.COLUMN_BATCH, batch.getText().toString().trim());

                long result = db.insert(StudentEntry.TABLE_NAME, null, contentValues);

                if (result == -1) {
                    Toast.makeText(getApplicationContext(), "Data not Insert", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();
                    qr.setText("");
                    name.setText("");
                    batch.setText("");
                }
            }
        });

        //READ
        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());

                SQLiteDatabase db = studentDbHelper.getReadableDatabase();

                String[] projection = {StudentEntry._ID,
                        StudentEntry.COLUMN_QR_CODE_ID,
                        StudentEntry.COLUMN_NAME,
                        StudentEntry.COLUMN_BATCH};
                Cursor cursor = db.query(StudentEntry.TABLE_NAME, projection,
                        null, null,
                        null, null, null);

//                Cursor cursor = db.rawQuery("SELECT * FROM " + StudentEntry.TABLE_NAME, null);

                if (cursor.getCount() == 0) {
                    showMessage("Error 404", "No Record found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (cursor.moveToNext()) {
                    buffer.append("ID: " + cursor.getInt(0) + "\n");
                    buffer.append("QR_CODE_ID: " + cursor.getString(1) + "\n");
                    buffer.append("NAME: " + cursor.getString(2) + "\n");
                    buffer.append("BATCH: " + cursor.getString(3) + "\n\n");
                }
                showMessage("Data", buffer.toString());

                cursor.close();
            }
        });

        //UPDATE
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().toString().trim().equals("") || batch2.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());
                SQLiteDatabase db = studentDbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(StudentEntry.COLUMN_BATCH, batch2.getText().toString().trim());
                int result = db.update(StudentEntry.TABLE_NAME, contentValues, "_ID = ?", new String[]{id.getText().toString().trim()});

                if (result > 0) {
                    Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_LONG).show();
                    id.setText("");
                    batch2.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Data not Update", Toast.LENGTH_LONG).show();
                }
            }
        });

        //DELETE
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id2.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());
                SQLiteDatabase db = studentDbHelper.getWritableDatabase();
                int result = db.delete(StudentEntry.TABLE_NAME, "_ID = ?", new String[]{id2.getText().toString()});

                if (result > 0) {
                    Toast.makeText(MainActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                    id2.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Data not Delete", Toast.LENGTH_LONG).show();
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
