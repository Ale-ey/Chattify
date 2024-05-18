package com.example.chatify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingPage extends AppCompatActivity {

    ImageView setProf;
    EditText setName, setStatus;
    Button doneButton;

    String email, password;
    Uri setImageUri;


    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting_page);

        auth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage =FirebaseStorage.getInstance();

        setProf = findViewById(R.id.set_pro);
        setName= findViewById(R.id.nameUSer);
        setStatus =findViewById( R.id.statusUser);
        doneButton =findViewById(R.id.donebutt);


        DatabaseReference reference = database.getReference().child("user").child(auth.getCurrentUser().getUid());
        StorageReference storageReference = storage .getReference().child("upload").child(auth.getCurrentUser().getUid());
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                email= snapshot.child("mail").getValue().toString();
                password =snapshot.child("password").getValue().toString();

                String name = snapshot.child("userName").getValue().toString();
                String profile = "https://firebasestorage.googleapis.com/v0/b/chatify-d1df6.appspot.com/o/man.png?alt=media&token=6310bdd7-d7b6-40f0-b81d-5cacbbc56185";
                String status = snapshot.child("status").getValue().toString();
                setName.setText(name);
                setStatus.setText(status);
                Picasso.get().load(profile).into(setProf);
            }
        });
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                email= snapshot.child("mail").getValue().toString();
//                password =snapshot.child("password").getValue().toString();
//
//                String name = snapshot.child("userName").getValue().toString();
//                String profile = snapshot.child("profile_pic").getValue().toString();
//                String status = snapshot.child("status").getValue().toString();
//                setName.setText(name);
//                setStatus.setText(status);
//                Picasso.get().load(profile).into(setProf);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



        setProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("img/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Pic"),10);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = setName.getText().toString();
                String status =setStatus.getText().toString();
                if(setImageUri!=null){
                        storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String finalImageUri =uri.toString();
                                        Users users = new Users(finalImageUri,email,name,password,status,auth.getUid());
                                        reference.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(SettingPage.this, "Data is Save", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SettingPage.this,MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(SettingPage.this, "Error Occurs", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                }else{
                    String finalImageUri = "https://firebasestorage.googleapis.com/v0/b/chatify-d1df6.appspot.com/o/man.png?alt=media&token=6310bdd7-d7b6-40f0-b81d-5cacbbc56185";;
                    Users users = new Users(finalImageUri, email, name, password, status, auth.getUid());
                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SettingPage.this, "Data is Save", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SettingPage.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SettingPage.this, "Error Occurs", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                setImageUri = data.getData();
                setProf.setImageURI(setImageUri);
            }
        }


    }
}