package com.example.tutorkit.Tutor.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Calendar;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    Context context;
    ArrayList<Calendar> calendars;
    DatabaseReference databaseReference;


    public CalendarAdapter(Context context, ArrayList<Calendar> calendars) {
        this.context = context;
        this.calendars = calendars;
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

        Calendar calendar = calendars.get(position);

        holder.name.setText("Name : " + calendar.getName());
        holder.time.setText("Time : " + calendar.getTime());

        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, calendar.getID(), calendar.getName(), calendar.getTime());
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TuitionAdapter.ViewDialogConfirmDelete viewDialogConfirmDelete = new TuitionAdapter.ViewDialogConfirmDelete();
//                viewDialogConfirmDelete.showDialog(context, calendar.getID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return calendars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView time;
        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            time = itemView.findViewById(R.id.txt_time);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
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

            edt_name.setText(name);
            edt_time.setText(time);

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


                    databaseReference.child("tuition").child(id).setValue(new Calendar(id, s_name, s_time));
                    Toast.makeText(context, "User Updated successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}
