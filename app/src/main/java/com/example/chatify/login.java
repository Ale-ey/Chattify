package com.example.chatify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class login extends AppCompatActivity {
    Button button;
    EditText email, password;
    FirebaseAuth auth;
    String emailPattren = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextView logSingUp;

    android.app.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);





//        getSupportActionBar().hide();    // cause error


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser= auth.getCurrentUser();
        if (currentUser != null) {
            // If user is already logged in, navigate to MainActivity
            startActivity(new Intent(login.this, MainActivity.class));
            finish();
        }


        button = findViewById(R.id.rgsignupbutton);
        email= findViewById(R.id.rgEmail);
        password = findViewById(R.id.rgPassword);
        logSingUp= findViewById(R.id.log_sigup);

        logSingUp.setOnClickListener(v -> {
            Intent intent = new Intent(login.this,registration.class);
            startActivity(intent);
            finish();
        });

        button.setOnClickListener(v -> {
            String Email = email.getText().toString();
            String pass = password.getText().toString();

            if(TextUtils.isEmpty(Email)){
                progressDialog.dismiss();
                Toast.makeText(login.this, "Enter the Email", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(pass)) {
                progressDialog.dismiss();
                Toast.makeText(login.this, "Enter the Password", Toast.LENGTH_SHORT).show();

            } else if (!Email.matches(emailPattren)) {
                progressDialog.dismiss();
                email.setError("Give Correct Email Address");
            } else if (password.length()<6) {
                    progressDialog.dismiss();
                password.setError("Should More than 6 Character");
                Toast.makeText(login.this, "Pass need to be longer than 6 characters", Toast.LENGTH_SHORT).show();

            }else {
                auth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        progressDialog.show();
                        try{
                            Intent intent = new Intent(login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            Toast.makeText(login.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

            }

            // checking data



        });


    }
}