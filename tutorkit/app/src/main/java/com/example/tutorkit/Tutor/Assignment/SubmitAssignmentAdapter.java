package com.example.tutorkit.Tutor.Assignment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.tutorkit.Models.SubmitAssignmentModel;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Assignment.Submit;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SubmitAssignmentAdapter extends RecyclerView.Adapter<SubmitAssignmentAdapter.ViewHolder> {

    Context context;
    ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList;
    DatabaseReference databaseReference;

    public SubmitAssignmentAdapter(Context context, ArrayList<SubmitAssignmentModel> submitAssignmentModelArrayList) {
        this.context = context;
        this.submitAssignmentModelArrayList = submitAssignmentModelArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
//                viewDialogUpdate.showDialog(context, );
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
        public void showDialog(Context context, String id, String name, String dateline, int amount, int price) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_tuition);

            EditText edt_name = dialog.findViewById(R.id.edt_name);
            EditText edt_amount = dialog.findViewById(R.id.edt_amount);
            EditText edt_price = dialog.findViewById(R.id.edt_price);
            EditText edt_dateline = dialog.findViewById(R.id.edt_date);

            edt_name.setText(name);
            edt_amount.setText(String.valueOf(amount));
            edt_price.setText(String.valueOf(price));
            edt_dateline.setText(String.valueOf(dateline));

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
                    String s_name = edt_name.getText().toString();
                    int s_amount = Integer.parseInt(edt_amount.getText().toString());
                    int s_price = Integer.parseInt(edt_price.getText().toString());
                    String s_dateline = edt_dateline.getText().toString();

//                    databaseReference.child("tuition").child(id).setValue(new SubmitAssignment(id, s_name, s_dateline,"",s_amount, s_price));
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
