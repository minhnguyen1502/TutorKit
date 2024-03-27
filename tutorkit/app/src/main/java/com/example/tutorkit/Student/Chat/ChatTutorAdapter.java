package com.example.tutorkit.Student.Chat;

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
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Chat.Message.ChatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatTutorAdapter extends RecyclerView.Adapter<ChatTutorAdapter.viewholder> {
    Context context;
    ArrayList<Tutor> tutorArrayList;
    DatabaseReference databaseReference;

    public ChatTutorAdapter(Context context, ArrayList<Tutor> tutorArrayList) {
        this.context = context;
        this.tutorArrayList = tutorArrayList;
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

        Tutor tutor = tutorArrayList.get(position);
        holder.name.setText(tutor.getName());
        Glide
                .with(context)
                .load(tutor.getImg())
                .centerCrop()
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatTutorActivity.class);
                intent.putExtra("nameeee", tutor.getName());
                intent.putExtra("reciverImg", tutor.getImg());
                intent.putExtra("uid", tutor.getId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return tutorArrayList.size();
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
