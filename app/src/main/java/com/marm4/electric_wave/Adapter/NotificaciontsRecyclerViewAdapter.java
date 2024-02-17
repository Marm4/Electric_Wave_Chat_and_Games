package com.marm4.electric_wave.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.FriendController;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.User;
import java.util.List;

public class NotificaciontsRecyclerViewAdapter  extends RecyclerView.Adapter<NotificaciontsRecyclerViewAdapter.ViewHolder> {

    private List<User> requestList;

    public NotificaciontsRecyclerViewAdapter(List<User> userList) {
        this.requestList = userList;
    }

    @NonNull
    @Override
    public NotificaciontsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new NotificaciontsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificaciontsRecyclerViewAdapter.ViewHolder holder, int position) {
        User user = requestList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;
        private TextView userNameTV;
        private ImageView addIV;
        private ImageView messageIV;
        private ImageView acceptIV;
        private ImageView supplyIV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.name);
            userNameTV = itemView.findViewById(R.id.userName);
            addIV = itemView.findViewById(R.id.add);
            messageIV = itemView.findViewById(R.id.message);
            acceptIV = itemView.findViewById(R.id.accept);
            supplyIV = itemView.findViewById(R.id.supply);

            addIV.setVisibility(View.GONE);
            messageIV.setVisibility(View.GONE);

            acceptIV.setVisibility(View.VISIBLE);
            supplyIV.setVisibility(View.VISIBLE);
        }


        public void bind(User user) {
            nameTV.setText(user.getName());
            userNameTV.setText(user.getUserName());
            FriendController friendController = new FriendController();
            acceptIV.setOnClickListener(view -> {
                friendController.acceptDeleteFriend(user.getId(), true);
                CurrentUser.getInstance().deleteRequestList(user);
                CurrentUser.getInstance().addFriendList(user);
                itemView.setVisibility(View.GONE);
            });

            supplyIV.setOnClickListener(view -> {
                friendController.acceptDeleteFriend(user.getId(), false);
                CurrentUser.getInstance().deleteRequestList(user);
                itemView.setVisibility(View.GONE);
            });
        }
    }
}