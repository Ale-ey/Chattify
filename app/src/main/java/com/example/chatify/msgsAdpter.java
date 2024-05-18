package com.example.chatify;

import static com.example.chatify.chatWin.receiverIimg;
import static com.example.chatify.chatWin.senderimg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class msgsAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constants for identifying sender and receiver items
    private static final int ITEM_SEND = 1;
    private static final int ITEM_RECEIVE = 2;

    private  Context context;
    private  ArrayList<msgmodel> messageAdapterArrayList;

    public void setMessageAdapterArrayList(ArrayList<msgmodel> messageAdapterArrayList) {
        this.messageAdapterArrayList = messageAdapterArrayList;
    }

    // Constructor to initialize the adapter
    public msgsAdpter(Context context, ArrayList<msgmodel> messageAdapterArrayList) {
        this.context = context;
        // Initialize the message list, handling null case
        this.messageAdapterArrayList = messageAdapterArrayList ;
    }

    // Inflating the layout based on item type (sender or receiver)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_SEND) {
            view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SenderViewHolder(view); // Return sender view holder
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(view); // Return receiver view holder
        }
    }

    // Binding data to views based on item type (sender or receiver)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgmodel message = messageAdapterArrayList.get(position);
        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            // Set sender message text
            senderViewHolder.msgText.setText(message.getMessage());
            // Load sender image using Picasso
            Picasso.get().load(senderimg).into(senderViewHolder.imageView);
        } else if (holder instanceof ReceiverViewHolder) {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            // Set receiver message text
            receiverViewHolder.msgText.setText(message.getMessage());
            // Load receiver image using Picasso
            Picasso.get().load(receiverIimg).into(receiverViewHolder.imageView);
        }
    }

    // Get total number of items in the list
    @Override
    public int getItemCount() {
        return messageAdapterArrayList.size();
    }

    // Determine the view type of the item at the specified position
    @Override
    public int getItemViewType(int position) {
        msgmodel message = messageAdapterArrayList.get(position);
        // Check if the current user is the sender or receiver of the message
        if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(message.getSender_id())) {
            return ITEM_SEND; // Return sender item type
        } else {
            return ITEM_RECEIVE; // Return receiver item type
        }
    }

    // ViewHolder for sender views
    static class SenderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView msgText;

        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize sender views
            imageView = itemView.findViewById(R.id.profileSender);
            msgText = itemView.findViewById(R.id.msgSender);
        }
    }

    // ViewHolder for receiver views
    static class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView msgText;

        ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize receiver views
            imageView = itemView.findViewById(R.id.receiverImg);
            msgText = itemView.findViewById(R.id.receiver_text);
        }
    }
}