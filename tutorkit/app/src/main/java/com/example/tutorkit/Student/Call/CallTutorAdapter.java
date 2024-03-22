package com.example.tutorkit.Student.Call;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CallTutorAdapter extends RecyclerView.Adapter<CallTutorAdapter.ViewHolder>{
    Context context;
    ArrayList<Tutor> tutorArrayList;
    DatabaseReference databaseReference;
    public CallTutorAdapter(Context context, ArrayList<Tutor> tutorArrayList) {
        this.context = context;
        this.tutorArrayList = tutorArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.call_tutor, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tutor tutor = tutorArrayList.get(position);

         Glide
                .with(context)
                .load(tutor.getImg())
                .centerCrop()
                .into(holder.avatar);
        holder.txtName.setText(tutor.getName());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tutor.getPhone()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    // Handle the case where no activity can handle the intent
                    Toast.makeText(context, "No app available to handle this action", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return tutorArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView avatar;
        ImageView call;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtName = itemView.findViewById(R.id.name);
            call = itemView.findViewById(R.id.call);
        }
    }
}
