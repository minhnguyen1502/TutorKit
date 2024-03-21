package com.example.tutorkit.Tutor.Grade;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Grade;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Account.Edit_tutor_profile;
import com.example.tutorkit.Tutor.Tuition.TuitionAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {

    Context context;
    ArrayList<Grade> gradeArrayList;
    DatabaseReference databaseReference;

    ArrayList<Student> studentArrayList;


    public GradeAdapter(Context context, ArrayList<Grade> gradeArrayList) {
        this.context = context;
        this.gradeArrayList = gradeArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.grade, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeAdapter.ViewHolder holder, int position) {

        Grade grade = gradeArrayList.get(position);

        holder.type.setText(" " + grade.getType());
        holder.title.setText("Title: " + grade.getTitle());
        holder.grade.setText("Garde: " + grade.getGrade());
        holder.date.setText("Date : " + grade.getDate());

        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, grade.getId(), grade.getType(), grade.getTitle(), grade.getGrade(), grade.getDate());
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, grade.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return gradeArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView type;
        TextView title;
        TextView grade;
        TextView date;
        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.txt_type);
            title = itemView.findViewById(R.id.txt_title);
            grade = itemView.findViewById(R.id.txt_grade);
            date = itemView.findViewById(R.id.txt_date);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }

    public class ViewDialogUpdate {
        public void showDialog(Context context, String id, String type, String title, int grade, String date) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_grade);

            Spinner spn_type = dialog.findViewById(R.id.spn_type);
            EditText edt_title = dialog.findViewById(R.id.edt_title);
            EditText edt_grade = dialog.findViewById(R.id.edt_grade);
            EditText edt_date = dialog.findViewById(R.id.edt_date);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.type, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_type.setAdapter(adapter);

            spn_type.setAdapter(adapter);
            edt_title.setText(title);
            edt_grade.setText(String.valueOf(grade));
            edt_date.setText(date);

            String[] arrSubject = context.getResources().getStringArray(R.array.type);
            spn_type.setSelection(Arrays.asList(arrSubject).indexOf(type));


            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonUpdate.setText("UPDATE");

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String newType = spn_type.getSelectedItem().toString();
                    String newTitle = edt_title.getText().toString();
                    int newGrade = Integer.parseInt(edt_grade.getText().toString());
                    String newDate = edt_date.getText().toString();

//                    if (name.isEmpty() || email.isEmpty() || country.isEmpty()) {
//                        Toast.makeText(context, "Please Enter All data...", Toast.LENGTH_SHORT).show();
//                    } else {

//                        if (newType.equals(type) && newTitle.equals(title) && newGrade.equals(grade)&& newDate.equals(date)) {
//                            Toast.makeText(context, "you don't change anything", Toast.LENGTH_SHORT).show();
//                        } else {
                            databaseReference.child("grades").child(id).setValue(new Grade(id, newType, newTitle, newDate, newGrade));
                            Toast.makeText(context, "User Updated successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


//                    }
//                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }


    public class ViewDialogConfirmDelete {
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

                    databaseReference.child("grades").child(id).removeValue();
                    Toast.makeText(context, "Grade Deleted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}
