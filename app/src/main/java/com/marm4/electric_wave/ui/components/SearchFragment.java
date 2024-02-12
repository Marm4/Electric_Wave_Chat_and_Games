package com.marm4.electric_wave.ui.components;

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
import android.widget.TextView;

import com.marm4.electric_wave.Adapter.SearchRecyclerViewAdapter;
import com.marm4.electric_wave.Interface.OnSearchUserCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.AuthController;
import com.marm4.electric_wave.model.User;

import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchET;
    private View root;

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

        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                Log.i("TAG", "--- SEARCH ACTION ----");
                String name = searchET.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEND || !name.isEmpty()) {
                    searchUsers(name);
                    return true;
                }
                Log.i("TAG", "Bad request");
                return false;
            }
        });
    }

    private void searchUsers(String name){
        Log.i("TAG", "Good request, searching users");
        AuthController authController = new AuthController();
        authController.searchUsers(name, new OnSearchUserCompleteListener() {
            @Override
            public void onSearchUserComplete(List<User> userList) {
                showResults(userList);
            }

            @Override
            public void onSearchUserError(String errorMessage) {
                Log.e("TAG", errorMessage);
            }
        });

    }

    private void showResults(List<User> userList){
            RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(userList);
            recyclerView.setAdapter(adapter);
    }



}