package com.marm4.electric_wave.ui.main.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marm4.electric_wave.Adapter.NotificaciontsRecyclerViewAdapter;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.User;

import java.util.List;

public class NotificationsFragment extends Fragment {

    private View root;
    public NotificationsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        initUI();
        
        return root;
    }

    private void initUI() {
        List<User> requestList = CurrentUser.getInstance().getRequestList();

        if (requestList != null && !requestList.isEmpty()){
            RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            NotificaciontsRecyclerViewAdapter adapter = new NotificaciontsRecyclerViewAdapter(CurrentUser.getInstance().getRequestList());
            recyclerView.setAdapter(adapter);
        }
    }
}