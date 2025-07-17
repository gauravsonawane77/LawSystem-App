package com.example.lawsystem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Message> messages;
    private final int currentUserId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public MessageAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        public SentViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textSentMessage);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        public ReceivedViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textReceivedMessage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = messages.get(position);
        Log.d("ViewTypeCheck", "msg.senderId=" + msg.senderId + ", currentUserId=" + currentUserId);
        return msg.senderId == currentUserId ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sent_message, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message msg = messages.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).textMessage.setText(msg.message);
        } else {
            ((ReceivedViewHolder) holder).textMessage.setText(msg.message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}

