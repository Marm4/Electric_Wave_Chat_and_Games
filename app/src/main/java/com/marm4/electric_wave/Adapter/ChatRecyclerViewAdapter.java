package com.marm4.electric_wave.Adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.global.CurrentChat;
import com.marm4.electric_wave.model.Message;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private List<Message> chatList;

    public ChatRecyclerViewAdapter(List<Message> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.ViewHolder holder, int position) {
        Message message = chatList.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTV;
        private View root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            messageTV = root.findViewById(R.id.text);

        }


        public void bind(Message message) {
            messageTV.setText(message.getContent());
            if(!message.getSenderId().equals(CurrentChat.getInstance().getUser().getId())){
                Drawable backgroundPrimary = ContextCompat.getDrawable(root.getContext(), R.drawable.border_radius_primary);
                root.setBackground(backgroundPrimary);
            }
        }
    }
}