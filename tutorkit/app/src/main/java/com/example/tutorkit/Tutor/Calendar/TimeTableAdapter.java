package com.example.tutorkit.Tutor.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.TimeTable;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {
    Context context;
    ArrayList<TimeTable> timeTables;
    DatabaseReference databaseReference;
    ArrayList<Student> students = new ArrayList<>();
    int positionName =0;
    public TimeTableAdapter(Context context,ArrayList<TimeTable> timeTables) {
        this.context = context;
        this.timeTables = timeTables;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public void setStudent(ArrayList<Student> studentArrayList) {
        students = studentArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.calendar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TimeTable timeTable = timeTables.get(position);

        holder.name.setText("Name : " + timeTable.getName());
        holder.time.setText("Time : " + timeTable.getTime());
        holder.date.setText("Date: " + timeTable.getDate());

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, timeTable.getID(), timeTable.getName(), timeTable.getTime(),timeTable.getDate());
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
        public void showDialog(Context context, String id, String name, String time, String date) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_calendar);

            Spinner spn_name = dialog.findViewById(R.id.spn_name);
            EditText edt_time = dialog.findViewById(R.id.edt_time);

            edt_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialog();
                }
            });

            ArrayAdapter<Student> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, students) {
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                    view.setText(students.get(position).getName());
                    return view;
                }

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setText(students.get(position).getName());
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for (int i =0; i< students.size();i++){
                if (Objects.equals(students.get(i).getName(), name)){
                    positionName =i;
                }
                Log.e("TAG", "showDialog: "+ students.get(i).getName() );
            }
            spn_name.setAdapter(adapter);
            spn_name.setSelection(positionName);
            edt_time.setText(time);
            edt_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                    edt_time.setText(time);
                                    Toast.makeText(context, "Selected time: " + time, Toast.LENGTH_SHORT).show();
                                }
                            },
                            hour,
                            minute,
                            false);

                    timePickerDialog.show();
                }
            });

            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            ImageView buttonCancel = dialog.findViewById(R.id.cancel);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Student student = (Student) spn_name.getSelectedItem();
                    String idTutor = FirebaseAuth.getInstance().getUid();
                    String name = student.getName();
                    String time = edt_time.getText().toString();


                    if (TextUtils.isEmpty(time)) {
                        Toast.makeText(context, "Enter dateline", Toast.LENGTH_SHORT).show();
                        edt_time.setError("required");
                        edt_time.requestFocus();

                    } else {
                        databaseReference.child("calendar").child(id).setValue(new TimeTable(id, name, time, idTutor, student.getId(), date));
                        Toast.makeText(context, "User Updated successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
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
        public void showDialog(Context context, String id) {
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

