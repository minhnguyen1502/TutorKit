package com.example.tutorkit.Student.Assignment;

import static com.example.tutorkit.Tutor.Chat.Message.ChatActivity.senderImg;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tutorkit.Models.AssignmentModel;
import com.example.tutorkit.Models.SubmitAssignmentModel;
import com.example.tutorkit.R;
import com.example.tutorkit.Tutor.Assignment.SubmitAssignmentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubmitAdpater extends RecyclerView.Adapter<SubmitAdpater.ViewHolder> {

    Context context;
    ArrayList<AssignmentModel> assignmentModelArrayList;
    DatabaseReference databaseReference;

    public SubmitAdpater(Context context, ArrayList<AssignmentModel> assignmentModelArrayList) {
        this.context = context;
        this.assignmentModelArrayList = assignmentModelArrayList;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AssignmentModel assignmentModel = assignmentModelArrayList.get(position);

        Glide
                .with(context)
                .load(assignmentModel.getImg())
                .centerCrop()
                .into(holder.img);
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewDialogConfirmDelete viewDialogConfirmDelete = new ViewDialogConfirmDelete();
                viewDialogConfirmDelete.showDialog(context, assignmentModel.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return assignmentModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        ImageView img;
       ImageView close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            close = itemView.findViewById(R.id.close);
            img = itemView.findViewById(R.id.img);

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

                    databaseReference.child("Assignment").child(id).removeValue();
                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }
}
