package com.example.mohamed.chattapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.model.Message;
import com.example.mohamed.chattapp.model.MessageType;
import com.example.mohamed.chattapp.utils.Session;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private List<Message> messages;

    public MessagingAdapter(Context context, List<Message> messages){
        this.context =context;
        this.messages =messages;
    }

    @Override
    public int getItemViewType(int position) {
        int userId = Session.getInstance().getUser().getId();
        Message message =messages.get(position);
        if (userId ==Integer.parseInt(message.getUserId())){
            if (message.getType().equals("1")){
                return MessageType.SENT_TEXT;
            }
            else {
                return MessageType.SENT_IMG;
            }
        }
        else {
            if (message.getType().equals("1")){
                return MessageType.RECEIVED_TEXT;
            }
            else if (message.getType().equals("2")){
                return MessageType.RECEIVED_IMG;
            }
        }

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        if (viewType ==MessageType.SENT_TEXT){
            return new SentTextHolder(LayoutInflater.from(context).inflate(R.layout.rv_sent_message, parent, false));
        }
        else if (viewType ==MessageType.SENT_IMG){
            return new SentTextHolder(LayoutInflater.from(context).inflate(R.layout.rv_sent_image, parent, false));
        }
        else if (viewType ==MessageType.RECEIVED_TEXT){
            return new ReceivedTextHolder(LayoutInflater.from(context).inflate(R.layout.rv_received_message, parent, false));
        }
        else if (viewType ==MessageType.RECEIVED_IMG){
            return new ReceivedImgHolder(LayoutInflater.from(context).inflate(R.layout.rv_received_image, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type =getItemViewType(position);
        Message message =messages.get(position);

        if (type ==MessageType.SENT_TEXT){
            SentTextHolder sentTextHolder = (SentTextHolder) holder;
            sentTextHolder.tvTime.setText(message.getTimestamp());
            sentTextHolder.tvMessageContent.setText(message.getContent());
        }
        else if (type ==MessageType.SENT_IMG){
            SentImgHolder sentImgHolder = (SentImgHolder) holder;
            sentImgHolder.tvTime.setText(message.getTimestamp());
            Picasso.get().load(message.getContent()).into(sentImgHolder.imgSent);
        }
        else if (type ==MessageType.RECEIVED_TEXT){
            ReceivedTextHolder receivedTextHolder = (ReceivedTextHolder) holder;
            receivedTextHolder.tvTime.setText(message.getTimestamp());
            receivedTextHolder.tvMessageContent.setText(message.getContent());
        }
        else if (type ==MessageType.RECEIVED_IMG){
            ReceivedImgHolder receivedImgHolder = (ReceivedImgHolder) holder;
            receivedImgHolder.tvTime.setText(message.getTimestamp());
            Picasso.get().load(message.getContent()).into(receivedImgHolder.imgSent);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    //Sent Message Holders..
    class SentMessageHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        SentMessageHolder(View itemView) {
            super(itemView);
            tvTime =itemView.findViewById(R.id.tv_time);
        }
    }

    class SentTextHolder extends SentMessageHolder{

        TextView tvMessageContent;

        SentTextHolder(View itemView) {
            super(itemView);
            tvMessageContent =itemView.findViewById(R.id.tv_message_content);
        }
    }

    class SentImgHolder extends SentMessageHolder{

        ImageView imgSent;

        public SentImgHolder(View itemView) {
            super(itemView);
            imgSent =itemView.findViewById(R.id.img_sent);
        }
    }

    //Received Message Holders..
    class ReceivedMessageHolder extends RecyclerView.ViewHolder{

        TextView tvTime, tvUsername;
        ReceivedMessageHolder(View itemView) {
            super(itemView);
            tvTime =itemView.findViewById(R.id.tv_time);
            tvUsername =itemView.findViewById(R.id.tv_username);
        }
    }

    class ReceivedTextHolder extends ReceivedMessageHolder {

        TextView tvMessageContent;

        ReceivedTextHolder(View itemView) {
            super(itemView);
            tvMessageContent =itemView.findViewById(R.id.tv_message_content);
        }
    }

    class ReceivedImgHolder extends ReceivedMessageHolder {

        ImageView imgSent;

        ReceivedImgHolder(View itemView) {
            super(itemView);
            imgSent =itemView.findViewById(R.id.img_sent);
        }
    }
}