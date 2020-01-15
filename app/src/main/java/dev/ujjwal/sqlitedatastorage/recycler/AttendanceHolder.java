package dev.ujjwal.sqlitedatastorage.recycler;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ujjwal.sqlitedatastorage.R;

class AttendanceHolder extends RecyclerView.ViewHolder {

    TextView textView;
    CheckBox checkBox;

    AttendanceHolder(@NonNull final View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.attendance_layout_tv);
        checkBox = itemView.findViewById(R.id.attendance_layout_checkBox);
    }
}