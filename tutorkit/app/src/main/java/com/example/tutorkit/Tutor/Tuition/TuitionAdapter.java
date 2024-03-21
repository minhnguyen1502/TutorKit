package com.example.tutorkit.Tutor.Tuition;

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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TuitionAdapter extends RecyclerView.Adapter<TuitionAdapter.ViewHolder> {

    Context context;
    ArrayList<Tuition> tuitionArrayList;
    DatabaseReference databaseReference;
    ArrayList<Student> studentArrayList;


    public TuitionAdapter(Context context, ArrayList<Tuition> tuitionArrayList) {
        this.context = context;
        this.tuitionArrayList = tuitionArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        studentArrayList = new ArrayList<>();
    }

    public void setDataStudent(ArrayList<Student> studentArrayList ){
        this.studentArrayList.clear();
        this.studentArrayList = studentArrayList;

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

        holder.name.setText("Name : " + tuition.getName());
        holder.amount.setText("Amount : " + tuition.getAmount());
        holder.price.setText("Price : " + tuition.getPrice());
        holder.dateline.setText("Dateline : " + tuition.getDateline());
        holder.total.setText(String.valueOf(tuition.getPrice()*tuition.getAmount()));

        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, tuition.getID(), tuition.getName(), tuition.getDateline(), tuition.getAmount(), tuition.getPrice());

            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
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
        Button buttonDelete;
        Button buttonUpdate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            amount = itemView.findViewById(R.id.txt_amount);
            price = itemView.findViewById(R.id.txt_price);
            dateline = itemView.findViewById(R.id.txt_date);
            total = itemView.findViewById(R.id.txt_total);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
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

//            for (int i =0; i<studentArrayList.size();i++){
//                if (Objects.equals(studentArrayList.get(i).getName(), name)){
//                    studentArrayList.remove(0);
//                    studentArrayList.add(new Student(name,"", "","","","",""));
//                }
//            }
            ArrayAdapter<Student> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, studentArrayList){
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                    view.setText(studentArrayList.get(position).getName());
                    return view;
                }
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    TextView view = (TextView) super.getView(position, convertView, parent);
                    view.setText(studentArrayList.get(position).getName());
                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_name.setAdapter(adapter);

            spn_name.setAdapter(adapter);
            edt_amount.setText(String.valueOf(amount));
            edt_price.setText(String.valueOf(price));
            edt_dateline.setText(dateline);

//            String[] arrSubject = context.getResources().getStringArray(studentArrayList);
//            spn_name.setSelection(Arrays.asList(arrSubject).indexOf(name));

            Button buttonUpdate = dialog.findViewById(R.id.buttonAdd);
            buttonUpdate.setText("Update");
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
                    String s_name = spn_name.getSelectedItem().toString();
                    int s_amount = Integer.parseInt(edt_amount.getText().toString());
                    int s_price = Integer.parseInt(edt_price.getText().toString());
                    String s_dateline = edt_dateline.getText().toString();

                            databaseReference.child("tuition").child(id).setValue(new Tuition(id, s_name, s_dateline,"","",s_amount, s_price));
                            Toast.makeText(context, "User Updated successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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

