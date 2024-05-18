package com.example.chatify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatWin extends AppCompatActivity {

    // Variables declaration
    String reciverImg, receiverName, receiverUid, senderUid;
    ImageView profilePic;
    TextView recName;
    CardView send;
    EditText textsend;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderimg;
    public static String receiverIimg;
    String senderRoom, receiverRoom;
    RecyclerView messagesAdapter;
    msgsAdpter messageadapter;
    ArrayList<msgmodel> messagesArraylsit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_win);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        // Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Bind UI elements
        send = findViewById(R.id.sendBtn);
        textsend = findViewById(R.id.writeMsg);
        messagesAdapter = findViewById(R.id.chatsID);

        // Retrieve data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            receiverName = intent.getStringExtra("nameeeee");
            reciverImg = intent.getStringExtra("receiverImg");
            receiverUid = intent.getStringExtra("uid");
        }

        // Bind other UI elements
        profilePic = findViewById(R.id.profile_chat);
        recName = findViewById(R.id.receiver_name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagesAdapter.setLayoutManager(linearLayoutManager);
        messagesArraylsit = new ArrayList<>();
        messageadapter = new msgsAdpter(chatWin.this, messagesArraylsit);
        messagesAdapter.setAdapter(messageadapter);

        // Load receiver's image using Picasso
        Picasso.get().load(reciverImg).into(profilePic);


        senderUid = firebaseAuth.getUid();
        if (senderUid.compareTo(receiverUid) > 0){
            senderRoom = senderUid + receiverUid;
        }
        else{
            senderRoom = receiverUid + senderUid;
        }
//        senderRoom = senderUid + receiverUid;
//        receiverRoom = receiverUid + senderUid;

        // Set receiver's name
        recName.setText(receiverName);

        DatabaseReference reference = database.getReference().child("user").child(senderUid);
        DatabaseReference chatreference = database.getReference().child("messages").child(senderRoom);

        chatreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                msgmodel messages = snapshot.getValue(msgmodel.class);
                messagesArraylsit.add(messages);
                messageadapter.setMessageAdapterArrayList(messagesArraylsit);
                messageadapter.notifyItemInserted(messagesArraylsit.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderimg = "https://firebasestorage.googleapis.com/v0/b/chatify-d1df6.appspot.com/o/man.png?alt=media&token=6310bdd7-d7b6-40f0-b81d-5cacbbc56185";
                reciverImg="https://firebasestorage.googleapis.com/v0/b/chatify-d1df6.appspot.com/o/man.png?alt=media&token=6310bdd7-d7b6-40f0-b81d-5cacbbc56185";
                receiverIimg = reciverImg; // Set receiver's image
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Listen for new messages in the chat



        // Send button click listener
        send.setOnClickListener(v -> {
            String message = textsend.getText().toString();
            if (message.isEmpty()) {
                Toast.makeText(chatWin.this, "Enter the message first", Toast.LENGTH_SHORT).show();
                return;
            }
            // Clear text input
            textsend.setText("");
            // Create message object
            Date date = new Date();
            msgmodel messages = new msgmodel(message, senderUid, date.getTime());

            database =FirebaseDatabase.getInstance();
            database.getReference().child("messages").child(senderRoom).push().setValue(messages);

//

        });
    }
}