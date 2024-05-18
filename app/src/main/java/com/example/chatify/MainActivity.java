package com.example.chatify;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView main_user_recyclerview;
    UserAdapter adapter;
    ArrayList<Users> userArrayList;
    ImageView image_logout, cam_btn, set_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        database=FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        set_btn = findViewById(R.id.settButtn);
        cam_btn = findViewById(R.id.cambtn);

        userArrayList= new ArrayList<>();
        main_user_recyclerview=findViewById(R.id.main_user_recyclerview);
        main_user_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter= new UserAdapter(MainActivity.this,userArrayList);
        main_user_recyclerview.setAdapter(adapter);

        DatabaseReference reference = database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list to avoid duplicates
                userArrayList.clear();

                // Get the current user's UID
                String currentUserUid = auth.getCurrentUser().getUid();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    // Get user data from the snapshot
                    Users users = dataSnapshot.getValue(Users.class);

                    // Check if the user is not the current user
                    if (!users.getUserId().equals(currentUserUid)) {
                        // Add the user to the list if it's not the current user
                        userArrayList.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        image_logout = findViewById(R.id.logout_main);
        image_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this,R.style.dialog);
                dialog.setContentView(R.layout.dialog_layout);
                Button by, bn;
                by= dialog.findViewById(R.id.log_yes);
                bn = dialog.findViewById(R.id.lout_no);
                by.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i1= new Intent(MainActivity.this, login.class);
                        startActivity(i1);
                        finish();
                    }
                });
                bn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iset= new Intent(MainActivity.this,SettingPage.class);
                startActivity(iset);
            }
        });

        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent icam =new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(icam,10);
            }
        });


        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this,login.class );
            startActivity(intent);
            finish();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}