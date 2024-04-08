package com.example.tutorkit.Tutor.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.TimeTable;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {
    Context context;
    ArrayList<TimeTable> timeTables;
    DatabaseReference databaseReference;
    int hour, minute;

    public void addData(ArrayList<TimeTable> timeTables){
        this.timeTables.clear();
        this.timeTables.addAll(timeTables);
        notifyDataSetChanged();

    }
    public TimeTableAdapter(Context context) {
        this.context = context;
        this.timeTables = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.calendar, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TimeTable timeTable = timeTables.get(position);

        holder.name.setText("Name : " + timeTable.getName());
        holder.time.setText("Time : " + timeTable.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        holder.date.setText("Date: " + formatter.format(timeTable.getDate()));

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, timeTable.getID(), timeTable.getName(), timeTable.getTime());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, timeTable.getID());
                }
        });
    }

    @Override
    public int getItemCount() {
        return timeTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        TextView date;
        ImageView delete;
        ImageView update;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            time = itemView.findViewById(R.id.txt_time);
            date = itemView.findViewById(R.id.txt_date);

            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }
    }

    public class ViewDialogUpdate {
        public void showDialog(Context context, String id, String name, String time) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_calendar);

            EditText edt_name = dialog.findViewById(R.id.edt_name);
            EditText edt_time = dialog.findViewById(R.id.edt_time);

            edt_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialog();
                }
            });
            edt_name.setText(name);
            edt_time.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));

            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String s_name = edt_name.getText().toString();
                    String s_time = edt_time.getText().toString();

//                    databaseReference.child("tuition").child(id).setValue(new TimeTable(id, s_name, s_time));
                    Toast.makeText(context, "User Updated successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
        
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Do something with the selected time
                        String time = hourOfDay + ":" + minute;
                        Toast.makeText(context, "Selected time: " + time, Toast.LENGTH_SHORT).show();
                    }
                },
                hour,
                minute,
                false);

        timePickerDialog.show();
    }

    private class ViewDialogConfirmDelete {
            public void showDialog(Context context, String id){
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_delete);

                Button buttonDelete = dialog.findViewById(R.id.buttonDelete);
                Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        databaseReference.child("calendar").child(id).removeValue();
                        Toast.makeText(context, "calendar Deleted successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        }
    }

