package com.example.tutorkit.Tutor.Assignment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.SubmitAssignmentModel;
import com.example.tutorkit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SubmitAssignmentAdapter extends RecyclerView.Adapter<SubmitAssignmentAdapter.ViewHolder> {

    Context context;
    ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList;
    DatabaseReference databaseReference;
    ArrayList<Student> students;


    public SubmitAssignmentAdapter(Context context, ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList) {
        this.context = context;
        this.submitAssignmentModelArrayList = submitAssignmentModelArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public void setDataStudent(ArrayList<Student> studentArrayList) {
        students = new ArrayList<>();
        students = studentArrayList;
    }

    @NonNull
    @Override
    public SubmitAssignmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.submit_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmitAssignmentAdapter.ViewHolder holder, int position) {

        SubmitAssignmentModel submitAssignmentModel = submitAssignmentModelArrayList.get(position);

        holder.name.setText("Name : " + submitAssignmentModel.getName());
        holder.dateline.setText("Dateline : " + submitAssignmentModel.getDateline());
        holder.title.setText("Title : " + submitAssignmentModel.getTitle());

        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, submitAssignmentModel.getId(), submitAssignmentModel.getName(), submitAssignmentModel.getDateline(), submitAssignmentModel.getTitle());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewSubmit.class);
                i.putExtra("idSubmit", submitAssignmentModel.getId());
                context.startActivity(i);

            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, submitAssignmentModel.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return submitAssignmentModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView title;
        TextView dateline;
        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            title = itemView.findViewById(R.id.txt_title);
            dateline = itemView.findViewById(R.id.txt_date);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }

    public class ViewDialogUpdate {
        public void showDialog(Context context, String id, String name, String dateline, String title) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_submit_assignment);

            Spinner spn_name = dialog.findViewById(R.id.spn_name);
            EditText edtDateline = dialog.findViewById(R.id.edt_date);
            EditText edtTitle = dialog.findViewById(R.id.edt_title);

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

            spn_name.setAdapter(adapter);
            edtTitle.setText(title);
            edtDateline.setText(dateline);

            edtDateline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                    edtDateline.setText(selectedDate);
                                }
                            },
                            // Truyền ngày hiện tại làm ngày mặc định cho DatePickerDialog.
                            Calendar.getInstance().get(Calendar.YEAR),
                            Calendar.getInstance().get(Calendar.MONTH),
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    );

                    datePickerDialog.show();
                }
            });
            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            buttonUpdate.setText("Update");

            ImageView cancel = dialog.findViewById(R.id.cancel);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });



            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Student student = (Student) spn_name.getSelectedItem();
                    String n_dateline = edtDateline.getText().toString();
                    String n_title =edtTitle.getText().toString();
                    String idTutor = FirebaseAuth.getInstance().getUid();
                    String n_name = student.getName();


                    if (TextUtils.isEmpty(n_dateline)){
                        Toast.makeText(context, "Enter dateline", Toast.LENGTH_SHORT).show();
                        edtDateline.setError("required");
                        edtDateline.requestFocus();

                    }      else if (TextUtils.isEmpty(n_title)){
                        Toast.makeText(context, "Enter title", Toast.LENGTH_SHORT).show();
                        edtTitle.setError("required");
                        edtTitle.requestFocus();
                    }else {
                        {
                            // Check if the selected date is in the future
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date selectedDate = sdf.parse(edtDateline.getText().toString());
                                Date currentDate = new Date();

                                if (selectedDate != null && selectedDate.after(currentDate)) {
                                    // The selected date is in the future, proceed to add the assignment
                                    databaseReference.child("submitAssignment").child(id).setValue(new SubmitAssignmentModel(id, n_title, n_dateline, idTutor, student.getId(), n_name));
                                    Toast.makeText(context, " Updated successfully!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    // The selected date is not in the future
                                    Toast.makeText(context, "Please select a future date", Toast.LENGTH_SHORT).show();
                                    edtDateline.requestFocus();
                                    edtDateline.setError("Invalid date");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                // Handle parsing exception if needed
                            }
//
                        }
                    }

                }
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

                    databaseReference.child("submitAssignment").child(id).removeValue();
                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}
