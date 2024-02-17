package com.marm4.electric_wave.Adapter;

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

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private List<User> userList;

    public SearchRecyclerViewAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("TAG", "--- SEARCH RECYCLER VIEW ---");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        Log.i("TAG", "Item count: " + getItemCount());
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

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
            friendController = new FriendController();

        }

        private void loadData(User user, Boolean requestExist) {
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
            friendController.requestExist(user.getId(), new OnFriendRequestCompleteListener() {
                @Override
                public void onFriendRequestComplete(Boolean friendRequest) {
                    loadData(user, friendRequest);

                }

            });
        }
    }
}