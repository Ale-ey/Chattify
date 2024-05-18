package com.example.chatify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class registration extends AppCompatActivity {
    TextView login_button;
    EditText rg_user_name, rg_email, rg_password;
    Button rg_sigUp;
    ImageView rg_profile;
    Uri image_uri;
    String image_uri_string;
    String emailPattren = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Establishing your Account...");
        progressDialog.setCancelable(false);

//        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        login_button = findViewById(R.id.rglogButton);
        rg_user_name = findViewById(R.id.statusUser);
        rg_email= findViewById(R.id.rgEmail);
        rg_password = findViewById(R.id.rgPassword);
        rg_sigUp = findViewById(R.id.rgsignupbutton);
        rg_profile = findViewById(R.id.rgprofilep1);

        login_button.setOnClickListener(v -> {
            Intent intent = new Intent(registration.this, login.class);
            startActivity(intent);
            finish();
        });

        rg_sigUp.setOnClickListener(v -> {
            String s_name = rg_user_name.getText().toString();
            String s_email = rg_email.getText().toString();
            String s_pass = rg_password.getText().toString();
            String status = "I am using Chattify";

            if (TextUtils.isEmpty(s_name) || TextUtils.isEmpty(s_email) || TextUtils.isEmpty(s_pass)) {
                progressDialog.dismiss();
                Toast.makeText(registration.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
            } else if (!s_email.matches(emailPattren)) {
                progressDialog.dismiss();
                rg_email.setError("Type a Valid Email");
            } else if (s_pass.length() < 6) {
                progressDialog.dismiss();
                rg_password.setError("Must be Six characters or more");
            } else {
                auth.createUserWithEmailAndPassword(s_email, s_pass).addOnCompleteListener(task -> {
//                            DatabaseReference reference ;
//                    DatabaseReference reference; = null;
                    if (task.isSuccessful()) {
                        String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        DatabaseReference reference = database.getReference().child("user").child(id);
                        StorageReference storageReference = storage.getReference().child("Upload").child(id);
                        if (image_uri != null) {
//                              DatabaseReference finalReference = reference;

                            storageReference.putFile(image_uri).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                        image_uri = uri;

                                        //profile_pic, mail, userName, password, userId, lastMessage, Status
                                        Users users = new Users(image_uri_string, s_email, s_name, s_pass, status,id);
                                        reference.setValue(users).addOnCompleteListener(task11 -> {
                                            if (task11.isSuccessful()) {
                                                progressDialog.show();
                                                Intent intent1 = new Intent(registration.this, MainActivity.class);
                                                startActivity(intent1);
                                                finish();
                                            } else {
                                                Toast.makeText(registration.this, "Error in Creating User", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    });
                                } else {
                                    Toast.makeText(registration.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            });

                        }else {
//                            String status1 = "Hey I am using this Chattily";
                            image_uri_string = "https://firebasestorage.googleapis.com/v0/b/chatify-d1df6.appspot.com/o/man.png?alt=media&token=6310bdd7-d7b6-40f0-b81d-5cacbbc56185";
                            Users users = new Users(image_uri_string, s_email, s_name, s_pass, status,id);
                            reference.setValue(users).addOnCompleteListener(task11 -> {
                                if (task11.isSuccessful()) {
                                    progressDialog.show();
                                    Intent intent2 = new Intent(registration.this, MainActivity.class);
                                    startActivity(intent2);
                                    finish();
                                } else {
                                    Toast.makeText(registration.this, "Error in Creating User", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }


                    }


                });
            }

        });


        rg_profile.setOnClickListener(v -> {
            Intent intent3 = new Intent();
            intent3.setType("image/*");
            intent3.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent3, "Select Picture"), 10);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                image_uri = data.getData();
                rg_profile.setImageURI(image_uri);


            }
        }
    }
}