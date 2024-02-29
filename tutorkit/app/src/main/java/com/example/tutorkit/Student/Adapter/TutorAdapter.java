package com.example.tutorkit.Student.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.request.target.ViewTarget;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Student.Student_home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TutorAdapter extends RecyclerView.Adapter<TutorAdapter.ViewHolder>{
    Context context;
    ArrayList<Tutor> tutorsArrayList;
    DatabaseReference databaseReference;

    public TutorAdapter(Context context, ArrayList<Tutor> usersItemArrayList) {
        this.context = context;
        this.tutorsArrayList = usersItemArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public TutorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tutor, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull TutorAdapter.ViewHolder holder, int position) {

        Tutor tutors = tutorsArrayList.get(position);

//         Glide
//                .with(TutorAdapter.this)
//                .load(tutors.getImg())
//                .centerCrop()
//                .into(holder.avatar);
        holder.txtName.setText(tutors.getName());
        holder.txtSubject.setText(tutors.getSubject());

        holder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               FirebaseDatabase.getInstance().getReference("tutors")
                       .child(tutors.getId())
                       .child("pick").setValue(!tutors.getPick());
                FirebaseDatabase.getInstance().getReference("tutors")
                        .child(tutors.getId())
                        .child("listIdStudent").child(FirebaseAuth.getInstance().getUid()).setValue("");
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtSubject;
        ImageView avatar;

        Button choose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            txtName = itemView.findViewById(R.id.name);
            txtSubject = itemView.findViewById(R.id.subject);

            choose = itemView.findViewById(R.id.btn_choose);
        }
    }
}
