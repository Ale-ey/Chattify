package com.example.chatify;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Users> userArrayList;

    public UserAdapter(MainActivity mainActivity, ArrayList<Users> userArrayList) {
        this.mainActivity=mainActivity;
        this.userArrayList=userArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mainActivity).inflate(R.layout.user_items,parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
        Users users = userArrayList.get(position);
        holder.user_name.setText(users.userName);
        holder.user_status.setText(users.status);
        Picasso.get().load(users.profile_pic).into(holder.user_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_chat =new Intent(mainActivity,chatWin.class);
                intent_chat .putExtra("nameeeee",users.getUserName());
                intent_chat .putExtra("receiverImg",users.getProfile_pic());
                intent_chat .putExtra("uid",users.getUserId());
                String uid = users.getUserId();


                Log.d("YourTag", "UID: " + uid);


                mainActivity.startActivity(intent_chat);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        ImageView user_image;
        TextView user_name, user_status;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            user_image = itemView.findViewById(R.id.profile_view);
            user_name = itemView.findViewById(R.id.profile_name);
            user_status= itemView.findViewById(R.id.profile_status);




        }
    }
}
