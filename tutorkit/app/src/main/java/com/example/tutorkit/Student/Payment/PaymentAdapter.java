package com.example.tutorkit.Student.Payment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tutorkit.Models.Tuition;
import com.example.tutorkit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference("tutors")
                .child(tuition.getIdTutor());
        tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tutorName = dataSnapshot.child("name").getValue(String.class);
                    if (tutorName != null) {
                        holder.name.setText("Tutor: " + tutorName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error
                Log.e("AssignmentAdapter", "Failed to read tutor data", databaseError.toException());
            }
        });
        holder.amount.setText("Amount : " + tuition.getAmount());
        holder.price.setText("Price : " + tuition.getPrice());
        holder.dateline.setText("Dateline : " + tuition.getDateline());
        int n_total = tuition.getPrice()*tuition.getAmount();
        holder.total.setText(String.valueOf(n_total));
        if (tuition.isStatus()) { // assuming isStatus() returns the status as boolean
            holder.status.setText("Done");
            holder.status.setTextColor(Color.GREEN);
            holder.buttonUpdate.setText("Done");
            holder.buttonUpdate.setFocusable(false);
        } else {
            holder.status.setText("Not Yet");
            holder.status.setTextColor(Color.RED);
            holder.buttonUpdate.setText("Payment");
            holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PayPalPaymentActivity.class);
                    i.putExtra("total", n_total);
                    i.putExtra("tuitionId", tuition.getID());
                    context.startActivity(i);
                }
            });
        }
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
        TextView status;
        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txt_name);
            amount = itemView.findViewById(R.id.txt_amount);
            price = itemView.findViewById(R.id.txt_price);
            dateline = itemView.findViewById(R.id.txt_date);
            total = itemView.findViewById(R.id.txt_total);
            status = itemView.findViewById(R.id.status);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }
}
