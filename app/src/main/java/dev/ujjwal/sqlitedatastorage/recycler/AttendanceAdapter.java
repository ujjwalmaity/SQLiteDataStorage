package dev.ujjwal.sqlitedatastorage.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dev.ujjwal.sqlitedatastorage.R;
import dev.ujjwal.sqlitedatastorage.AttendanceActivity;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceHolder> {

    private Context context;
    private Integer[] id;
    private String[] name;
    private Integer[] attendance;

    public AttendanceAdapter(Context context, Integer[] id, String[] name, Integer[] attendance) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.attendance = attendance;
    }

    @NonNull
    @Override
    public AttendanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_attendance, parent, false);
        return (new AttendanceHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceHolder holder, final int position) {
        String text = name[position];
        holder.name.setText(text);
        holder.checkBox.setText(id[position] + "");
        if (attendance[position].equals(0))
            holder.checkBox.setChecked(false);
        else if (attendance[position].equals(1))
            holder.checkBox.setChecked(true);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(false);
                    ((AttendanceActivity) context).makeAttendance(context, id[position], 0);
                    attendance[position] = 0;
                } else {
                    holder.checkBox.setChecked(true);
                    ((AttendanceActivity) context).makeAttendance(context, id[position], 1);
                    attendance[position] = 1;

                }
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    ((AttendanceActivity) context).makeAttendance(context, id[position], 1);
                    attendance[position] = 1;
                } else {
                    ((AttendanceActivity) context).makeAttendance(context, id[position], 0);
                    attendance[position] = 0;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.length;
    }
}