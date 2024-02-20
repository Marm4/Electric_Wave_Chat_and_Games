package com.marm4.electric_wave.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.marm4.electric_wave.R;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.Message;
import com.marm4.electric_wave.model.User;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_FRIEND = 2;
    private List<Message> messages;
    private User currentUser;
    private Uri userPP;
    private Uri friendPP;

    public ChatRecyclerViewAdapter(List<Message> messages, Uri userPP, Uri friendPP) {
        this.messages = messages;
        this.currentUser = CurrentUser.getInstance().getUser();
        this.userPP = userPP;
        this.friendPP = friendPP;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getSenderId().equals(currentUser.getId()) ? VIEW_TYPE_USER : VIEW_TYPE_FRIEND;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        if (viewType == VIEW_TYPE_USER) {
            itemView = inflater.inflate(R.layout.item_chat, parent, false);
            return new UserViewHolder(itemView, userPP);
        } else {
            itemView = inflater.inflate(R.layout.item_chat_friend, parent, false);
            return new FriendViewHolder(itemView, friendPP);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       Message message = messages.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message);
        } else if (holder instanceof FriendViewHolder) {
            ((FriendViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePictureIV;
        private TextView messageTV;
        private View root;

        public UserViewHolder(@NonNull View itemView, Uri userPP) {
            super(itemView);
            root = itemView;
            messageTV = root.findViewById(R.id.text);
            profilePictureIV = root.findViewById(R.id.profilePicture);
            profilePictureIV.setImageURI(userPP);
        }

        public void bind(Message message) {
            Log.i("ChatRecyclerViewAdapter", "User");
            messageTV.setText(message.getContent());
        }
    }

    private static class FriendViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePictureIV;
        private TextView messageTV;
        private View root;

        public FriendViewHolder(@NonNull View itemView, Uri friendPP) {
            super(itemView);
            root = itemView;
            messageTV = root.findViewById(R.id.text);
            profilePictureIV = root.findViewById(R.id.profilePicture);
            profilePictureIV.setImageURI(friendPP);
        }

        public void bind(Message message) {
            Log.i("ChatRecyclerViewAdapter", "Friend");
            messageTV.setText(message.getContent());
        }
    }
}
