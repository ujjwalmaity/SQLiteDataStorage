package dev.ujjwal.sqlitedatastorage.recycler;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ujjwal.sqlitedatastorage.R;

class AttendanceHolder extends RecyclerView.ViewHolder {

    TextView name;
    CheckBox checkBox;

    AttendanceHolder(@NonNull final View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.item_attendance_name);
        checkBox = itemView.findViewById(R.id.item_attendance_checkBox);
    }
}