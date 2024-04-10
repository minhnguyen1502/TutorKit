package com.example.tutorkit.Tutor.Tuition;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TuitionAdapter extends RecyclerView.Adapter<TuitionAdapter.ViewHolder> {

    Context context;
    ArrayList<Tuition> tuitionArrayList;
    DatabaseReference databaseReference;
    ArrayList<Student> students = new ArrayList<>();
    int positionName =0;

    public TuitionAdapter(Context context, ArrayList<Tuition> tuitionArrayList) {
        this.context = context;
        this.tuitionArrayList = tuitionArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void setDataStudent(ArrayList<Student> studentArrayList) {
        students = studentArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tuition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Tuition tuition = tuitionArrayList.get(position);

        holder.name.setText("Name: " + tuition.getName());
        holder.amount.setText("Amount : " + tuition.getAmount());
        holder.price.setText("Price : " + tuition.getPrice());
        holder.dateline.setText("Dateline : " + tuition.getDateline());
        holder.total.setText(String.valueOf(tuition.getPrice() * tuition.getAmount()));

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, tuition.getID(), tuition.getName(), tuition.getDateline(), tuition.getAmount(), tuition.getPrice());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, tuition.getID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return tuitionArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView amount;
        TextView price;
        TextView dateline;
        TextView total;
        Button delete;
        Button update;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            amount = itemView.findViewById(R.id.txt_amount);
            price = itemView.findViewById(R.id.txt_price);
            dateline = itemView.findViewById(R.id.txt_date);
            total = itemView.findViewById(R.id.txt_total);

            delete = itemView.findViewById(R.id.buttonDelete);
            update = itemView.findViewById(R.id.buttonUpdate);
        }
    }

    public class ViewDialogUpdate {
        public void showDialog(Context context, String id, String name, String dateline, int amount, int price) {

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_tuition);

            Spinner spn_name = dialog.findViewById(R.id.spn_name);
            EditText edt_amount = dialog.findViewById(R.id.edt_amount);
            EditText edt_price = dialog.findViewById(R.id.edt_price);
            EditText edt_dateline = dialog.findViewById(R.id.edt_date);

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
            edt_amount.setText(String.valueOf(amount));
            edt_price.setText(String.valueOf(price));
            edt_dateline.setText(dateline);

            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            buttonUpdate.setText("Update");
            edt_dateline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            context,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    // Người dùng đã chọn ngày. Cập nhật trường ngày (edate).
                                    String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                    edt_dateline.setText(selectedDate);
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
                    String idTutor = FirebaseAuth.getInstance().getUid();
                    String s_name = student.getName();
                    String s_dateline = edt_dateline.getText().toString();

                    int s_amount =0;
                    if (!TextUtils.isEmpty(edt_amount.getText().toString())){
                        s_amount = Integer.parseInt(edt_amount.getText().toString());
                    }
                    int s_price =0;
                    if (!TextUtils.isEmpty(edt_price.getText().toString())){
                        s_price = Integer.parseInt(edt_price.getText().toString());
                    }
                    if (TextUtils.isEmpty(s_dateline)){
                        Toast.makeText(context, "Enter dateline", Toast.LENGTH_SHORT).show();
                        edt_dateline.setError("required");
                        edt_dateline.requestFocus();

                    }
                    else if (TextUtils.isEmpty(edt_amount.getText().toString())){
                        Toast.makeText(context, "Enter amount", Toast.LENGTH_SHORT).show();
                        edt_amount.setError("required");
                        edt_amount.requestFocus();

                    }else if (TextUtils.isEmpty(edt_price.getText().toString())){
                        Toast.makeText(context, "enter price", Toast.LENGTH_SHORT).show();
                        edt_price.setError("required");
                        edt_price.requestFocus();

                    }
                    else {
                        // Check if the selected date is in the future

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date selectedDate = sdf.parse(edt_dateline.getText().toString());
                            Date currentDate = new Date();

                            if (selectedDate != null && selectedDate.after(currentDate)) {
                                // The selected date is in the future, proceed to add the assignment
                                databaseReference.child("tuition").child(id).setValue(new Tuition(id, s_name, s_dateline, student.getId(), idTutor, s_amount, s_price));
                                Toast.makeText(context, " Updated successfully!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                // The selected date is not in the future
                                Toast.makeText(context, "Please select a future date", Toast.LENGTH_SHORT).show();
                                edt_dateline.requestFocus();
                                edt_dateline.setError("Invalid date");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            // Handle parsing exception if needed
                        }
                    }

                    {

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

                    databaseReference.child("tuition").child(id).removeValue();
                    Toast.makeText(context, "tuition Deleted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}