package com.example.tutorkit.Tutor.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Chat.Message.ChatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatStudentAdapter extends RecyclerView.Adapter<ChatStudentAdapter.viewholder> {
    Context context;
    ArrayList<Student> studentArrayList;
    DatabaseReference databaseReference;

    public ChatStudentAdapter(Context context, ArrayList<Student> studentArrayList) {
        this.context = context;
        this.studentArrayList = studentArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_chat, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Student student = studentArrayList.get(position);
        holder.name.setText(student.getName());
        Glide
                .with(context)
                .load(student.getImg())
                .centerCrop()
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("nameeee", student.getName());
                intent.putExtra("reciverImg", student.getImg());
                intent.putExtra("uid", student.getId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return studentArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.userimg);
            name = itemView.findViewById(R.id.username);
        }
    }
}
