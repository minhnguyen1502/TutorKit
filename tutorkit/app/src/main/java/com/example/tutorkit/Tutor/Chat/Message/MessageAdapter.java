package com.example.tutorkit.Tutor.Chat.Message;

import static com.example.tutorkit.Tutor.Chat.Message.ChatActivity.reciverIImg;
import static com.example.tutorkit.Tutor.Chat.Message.ChatActivity.senderImg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.MessageModel;
import com.example.tutorkit.Models.Student;
import com.example.tutorkit.Models.Tutor;
import com.example.tutorkit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> messageModelArrayList;
    boolean isStudent;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;

    public MessageAdapter(Context context, ArrayList<MessageModel> messageModelArrayList, boolean isStudent) {
        this.context = context;
        this.isStudent = isStudent;
        this.messageModelArrayList = messageModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender, parent, false);
            return new senderVierwHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reciver, parent, false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messages = messageModelArrayList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Delete this message will update soon !!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });

        if (holder.getClass() == senderVierwHolder.class) {
            senderVierwHolder viewHolder = (senderVierwHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            String img;
            if (isStudent) {
                FirebaseDatabase.getInstance().getReference().child("Student").child(messages.getSenderid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Student student = snapshot.getValue(Student.class);
                        if (student == null) {
                            return;
                        }                        Glide
                                .with(context)
                                .load(student.getImg())
                                .centerCrop()
                                .into(viewHolder.circleImageView);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                FirebaseDatabase.getInstance().getReference().child("tutors").child(messages.getSenderid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Tutor tutor = dataSnapshot.getValue(Tutor.class);
                        if (tutor == null) {
                            return;
                        }                        Glide
                                .with(context)
                                .load(tutor.getImg())
                                .centerCrop()
                                .into(viewHolder.circleImageView);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        } else {
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
            String img2;
            if (isStudent) {

                FirebaseDatabase.getInstance().getReference().child("tutors").child(messages.getSenderid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Tutor tutor = dataSnapshot.getValue(Tutor.class);
                        if (tutor == null) {
                            return;
                        }                        Glide
                                .with(context)
                                .load(tutor.getImg())
                                .centerCrop()
                                .into(viewHolder.circleImageView);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {
                FirebaseDatabase.getInstance().getReference().child("Student").child(messages.getSenderid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Student student = snapshot.getValue(Student.class);
                        if (student == null) {
                            return;
                        }
                        Glide
                                .with(context)
                                .load(student.getImg())
                                .centerCrop()
                                .into(viewHolder.circleImageView);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messages = messageModelArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECIVE;
        }
    }

    class senderVierwHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public senderVierwHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);

        }
    }

    class reciverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView msgtxt;

        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}
