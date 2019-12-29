package dev.ujjwal.sqlitedatastorage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.database.Cursor;

import dev.ujjwal.sqlitedatastorage.data.StudentDbHelper;

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

                boolean isCreate = studentDbHelper.create(
                        qr.getText().toString(),
                        name.getText().toString(),
                        batch.getText().toString());
                if (isCreate) {
                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();
                    qr.setText("");
                    name.setText("");
                    batch.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Data not Insert", Toast.LENGTH_LONG).show();
                }
            }
        });

        //READ
        findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = studentDbHelper.read();

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

                boolean isUpdate = studentDbHelper.update(
                        id.getText().toString(),
                        batch2.getText().toString());
                if (isUpdate) {
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

                boolean isDelete = studentDbHelper.delete(id2.getText().toString());
                if (isDelete) {
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
