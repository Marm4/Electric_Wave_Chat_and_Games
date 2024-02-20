package com.marm4.electric_wave.ui.main.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.marm4.electric_wave.Adapter.SearchRecyclerViewAdapter;
import com.marm4.electric_wave.Interface.OnProfilePictureCompleteListener;
import com.marm4.electric_wave.Interface.OnSearchUserCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.AuthController;
import com.marm4.electric_wave.model.User;
import com.marm4.electric_wave.utility.DownloaderUtility;

import java.io.File;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchET;
    private ImageView sendIV;
    private View root;
    private SearchRecyclerViewAdapter adapter;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);

        initUI();

        return root;
    }

    private void initUI() {
        searchET = root.findViewById(R.id.search);
        sendIV = root.findViewById(R.id.send);

        sendIV.setOnClickListener(view -> {
            String name = searchET.getText().toString();
            searchET.setText("");
            if (!name.isEmpty()) {
                searchUsers(name.toLowerCase());
            }
        });

        initRV();
    }

    private void searchUsers(String name){
        Log.i("SearchFragment", "searching users");
        AuthController authController = new AuthController();
        authController.searchUserByUserName(name, new OnSearchUserCompleteListener() {
            @Override
            public void onSearchUserComplete(List<User> userList) {
                Log.i("SearchFragment", "Complete, showing results");
                showResults(userList);
            }

            @Override
            public void onSearchUserError(String errorMessage) {
                Log.i("SearchFragment", "Error : " + errorMessage);
            }
        });

    }

    private void showResults(List<User> userList){
        Log.i("SearchFragment", "showing results");

        int i = 0;
        while(i < userList.size()){
            Log.i("SearchFragment", "i: " + i + " userList size: " + userList.size());
            File file = new File(getContext().getExternalFilesDir(null), "Electric-Wave-PP");
            if(i == userList.size()-1)
                DownloaderUtility.setProfilePicture(userList.get(i), file, getContext(),new OnProfilePictureCompleteListener() {
                    @Override
                    public void onProfilePictureCompleteListener() {
                        Log.i("SearchFragment", "Listener return, going to initRV");
                            adapter.setList(userList);
                        }
                });
            else
                DownloaderUtility.setProfilePicture(userList.get(i), file, getContext(), null);
            i++;
        }

    }

    private void initRV() {
        Log.i("SearchFragment", "InitRV");
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }
}