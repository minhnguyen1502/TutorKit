package com.example.tutorkit.Student.Payment;

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

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    Context context;
    ArrayList<Tuition> tuitionArrayList;
    DatabaseReference databaseReference;

    public PaymentAdapter(Context context, ArrayList<Tuition> tuitionArrayList) {
        this.context = context;
        this.tuitionArrayList = tuitionArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tuition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {

        Tuition tuition = tuitionArrayList.get(position);

        holder.name.setText("Name : " + tuition.getName());
        holder.amount.setText("Amount : " + tuition.getAmount());
        holder.price.setText("Price : " + tuition.getPrice());
        holder.dateline.setText("Dateline : " + tuition.getDateline());
        holder.total.setText(String.valueOf(tuition.getPrice()*tuition.getAmount()));

       holder.buttonUpdate.setVisibility(View.GONE);
       holder.buttonDelete.setVisibility(View.GONE);

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
}
