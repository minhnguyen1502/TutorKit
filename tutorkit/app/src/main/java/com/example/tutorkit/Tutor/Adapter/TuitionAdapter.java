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

import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TuitionAdapter extends RecyclerView.Adapter<TuitionAdapter.ViewHolder> {

    Context context;
    ArrayList<Tuition> tuitionArrayList;
    DatabaseReference databaseReference;

    public TuitionAdapter(Context context, ArrayList<Tuition> tuitionArrayList) {
        this.context = context;
        this.tuitionArrayList = tuitionArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
        holder.dateline.setText("Name : " + tuition.getDateline());
        holder.total.setText(tuition.getTotal());


        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context, tuition.getID(), tuition.getName(), tuition.getDateline(), tuition.getAmount(), tuition.getPrice(), tuition.getTotal());
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
        public void showDialog(Context context, String id, String name, String dateline, int amount, int price, int total) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_add_tuition);

            EditText edt_name = dialog.findViewById(R.id.edt_name);
            EditText edt_amount = dialog.findViewById(R.id.edt_amount);
            EditText edt_price = dialog.findViewById(R.id.edt_price);
            EditText edt_dateline = dialog.findViewById(R.id.edt_date);
            EditText txt_total = dialog.findViewById(R.id.txt_total);

            edt_name.setText(name);
            edt_amount.setText(amount);
            edt_price.setText(price);
            edt_dateline.setText(dateline);
            txt_total.setText(total);


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
                    int s_amount = Integer.parseInt(edt_amount.getText().toString());
                    int s_price = Integer.parseInt(edt_price.getText().toString());
                    String s_dateline = edt_dateline.getText().toString();
                    int s_total = Integer.parseInt(txt_total.getText().toString());

                            databaseReference.child("tuition").child(id).setValue(new Tuition(id, s_name, s_dateline,s_amount, s_price,s_total));
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

