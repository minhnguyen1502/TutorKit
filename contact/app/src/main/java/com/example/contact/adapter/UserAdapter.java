package com.example.contact.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.contact.R;
import com.example.contact.User;
import com.example.contact.db.DBHelper;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    ArrayList<User> users;
    Context context;

    public UserAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.detail, parent, false);
        UserViewHolder vh = new UserViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user = users.get(position);
        holder.tvName.setText(user.getName());
        holder.tvDate.setText(user.getDate());
        holder.tvEmail.setText(user.getEmail());

        if (user.getImage() != null && !user.getImage().isEmpty()) {

            Glide.with(context).load(user.getImage()).into(holder.ivImage);
        }


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper = new DBHelper(context);
                int result = dbHelper.delete(user.getId());

                if (result > 0){
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                    users.remove(user);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDate, tvEmail;
        ImageView ivImage;
        Button  btnDelete;

        public UserViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvDate = v.findViewById(R.id.tvDate);
            tvEmail = v.findViewById(R.id.tvEmail);
            ivImage = v.findViewById(R.id.ivImage);

            btnDelete = v.findViewById(R.id.btnDelete);

        }
    }


}