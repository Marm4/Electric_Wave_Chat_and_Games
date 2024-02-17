package com.marm4.electric_wave.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marm4.electric_wave.Interface.OnChatCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.GroupChat;
import com.marm4.electric_wave.model.Message;
import com.marm4.electric_wave.model.User;

import java.util.Date;
import java.util.List;

public class GroupChatRecyclerViewAdapter extends RecyclerView.Adapter<GroupChatRecyclerViewAdapter.ViewHolder> {

    private List<User> requestList;
    private OnChatCompleteListener listener;

    public GroupChatRecyclerViewAdapter(List<User> userList, OnChatCompleteListener listener) {
        this.requestList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GroupChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new GroupChatRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatRecyclerViewAdapter.ViewHolder holder, int position) {
        User user = requestList.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        Log.i("Tag", "List size: " + requestList.size());
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userNameTV;
        private TextView lastMessageTV;
        private ImageView addIV;
        private ImageView messageIV;
        private ImageView acceptIV;
        private ImageView supplyIV;
        private View root;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            userNameTV = itemView.findViewById(R.id.userName);
            lastMessageTV = itemView.findViewById(R.id.name);
            addIV = itemView.findViewById(R.id.add);
            messageIV = itemView.findViewById(R.id.message);
            acceptIV = itemView.findViewById(R.id.accept);
            supplyIV = itemView.findViewById(R.id.supply);

            addIV.setVisibility(View.GONE);
            messageIV.setVisibility(View.GONE);
            acceptIV.setVisibility(View.GONE);
            supplyIV.setVisibility(View.GONE);
        }


        public void bind(User user, OnChatCompleteListener listener) {
            userNameTV.setText(user.getUserName());
            lastMessageTV.setText(lastMessage(user));

            root.setOnClickListener(view -> {
                listener.onChatClickListener(user);
            });
        }

        private String lastMessage(User user){
            List<GroupChat> groupChats = CurrentUser.getInstance().getChatGroupList();
            String lastMessage = "";
            for(GroupChat chat : groupChats){
                List<User> users = chat.getMembers();
                if(users.contains(user)){
                    List<Message> messages = chat.getMessages();
                    Date latestTimestamp = new Date(0);
                    for (Message message : messages) {
                        Date timestamp = message.getTimestamp();
                        if (timestamp.after(latestTimestamp)) {
                            latestTimestamp = timestamp;
                            lastMessage = message.getContent();
                        }
                    }
                }
            }
            return lastMessage;
        }
    }
}