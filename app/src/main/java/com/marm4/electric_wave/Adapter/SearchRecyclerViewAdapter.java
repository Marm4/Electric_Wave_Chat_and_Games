package com.marm4.electric_wave.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Tag;
import com.marm4.electric_wave.Interface.OnFriendRequestCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.FriendController;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.User;
import com.marm4.electric_wave.utility.DownloaderUtility;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private List<User> userList;

    public SearchRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(userList!=null){
            User user = userList.get(position);
            holder.bind(user);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<User> list) {
        userList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(userList==null)
            return 0;
        else
            return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePictureIV;
        private TextView nameTV;
        private TextView userNameTV;
        private ImageView addIV;
        private ImageView messageIV;
        private FriendController friendController;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.name);
            userNameTV = itemView.findViewById(R.id.userName);
            addIV = itemView.findViewById(R.id.add);
            messageIV = itemView.findViewById(R.id.message);
            profilePictureIV = itemView.findViewById(R.id.profilePicture);
            friendController = new FriendController();

        }

        private void loadData(User user, Boolean requestExist) {
            Log.i("SearchRecyclerViewAdapter", "Loading data...");
            List<User> friends = CurrentUser.getInstance().getFriendList();
            Boolean isFriend = false;
            for(User userAux : friends){
                if(userAux.getId().equals(user.getId()))
                    isFriend = true;
            }
            if(!requestExist && !isFriend)
                addIV.setOnClickListener(view -> {
                    friendController.sendFriendRequest(user.getId());
                    addIV.setVisibility(View.GONE);
                });
            else{
                addIV.setVisibility(View.GONE);
            }

            nameTV.setText(user.getName());
            userNameTV.setText(user.getUserName());
        }

        public void bind(User user) {
            Log.i("SearchRecyclerViewAdapter", "Setting view for: " + user.getUserName());
            profilePictureIV.setImageURI(user.getProfilePicture());
            friendController.requestExist(user.getId(), new OnFriendRequestCompleteListener() {
                @Override
                public void onFriendRequestComplete(Boolean friendRequest) {
                    loadData(user, friendRequest);
                }
            });
        }
    }
}