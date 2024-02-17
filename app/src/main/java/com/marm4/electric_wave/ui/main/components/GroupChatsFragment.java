package com.marm4.electric_wave.ui.main.components;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.marm4.electric_wave.Adapter.GroupChatRecyclerViewAdapter;
import com.marm4.electric_wave.Interface.OnChatCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.GroupChatController;
import com.marm4.electric_wave.global.CurrentChat;
import com.marm4.electric_wave.model.GroupChat;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.User;
import com.marm4.electric_wave.ui.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupChatsFragment extends Fragment {

    private EditText searchET;
    private ImageView sendIV;
    private List<GroupChat> chats;
    private User currentUser;
    private View root;
    private View container;

    public GroupChatsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_group_chat, container, false);
        initUI();
        return root;
    }

    private void initUI() {
        searchET = root.findViewById(R.id.search);
        sendIV = root.findViewById(R.id.send);
        chats = CurrentUser.getInstance().getChatGroupList();
        currentUser = CurrentUser.getInstance().getUser();


        sendIV.setOnClickListener(view -> {
            String name = searchET.getText().toString().toLowerCase();
            if (!name.isEmpty()) {
                searchGroupChat(name);
            }
        });

        if(!chats.isEmpty()){
            List<User> userChats = new ArrayList<>();
            for(GroupChat chat : chats){
                for(User user : chat.getMembers()){
                    if(!user.equals(currentUser)){
                        userChats.add(user);
                    }
                }
            }
            if(!userChats.isEmpty())
                showResults(userChats);
        }
    }

    private void searchGroupChat(String name) {
        List<User> usersFriend = CurrentUser.getInstance().getFriendList();
        List<User> usersFound = new ArrayList<>();

        if(!chats.isEmpty()){
            for(GroupChat chat : chats){
                List<User> usersAux = chat.getMembers();
                for(User user : usersAux){
                    if(!user.equals(currentUser) && (user.getUserName().contains(name) || user.getName().contains(name)))
                        usersFound.add(user);
                }
            }
        }

        if(!usersFriend.isEmpty()){
            for (User user : usersFriend){
                if((user.getName().contains(name) || user.getUserName().contains(name)) && !usersFound.contains(user))
                    usersFound.add(user);
            }
        }

        showResults(usersFound);
    }

    private void showResults(List<User> usersFound){
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        GroupChatRecyclerViewAdapter adapter = new GroupChatRecyclerViewAdapter(usersFound, new OnChatCompleteListener() {
            @Override
            public void onChatClickListener(User user) {
                CurrentChat.getInstance().setUser(user);
                searchChat(user);
                goToChat();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void searchChat(User user) {
        List<GroupChat> chats = CurrentUser.getInstance().getChatGroupList();
        GroupChat chat = null;
        for(GroupChat chatAux : chats){
            List<User> users = chatAux.getMembers();
            for (User userAux : users){
                if(userAux.equals(user) && !user.equals(currentUser)){
                    chat = chatAux;
                }
            }
        }

        if(chat == null){
            chat = new GroupChat();
            chat.addMembers(user);
            chat.addMembers(currentUser);
            saveChat(chat);
        }
        CurrentChat.getInstance().setGroupChat(chat);
    }

    private void saveChat(GroupChat chat) {
        CurrentUser.getInstance().addChatGroupList(chat);
        GroupChatController groupChatController = new GroupChatController();
        groupChatController.saveGroupChat(chat);
    }

    private void goToChat() {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        startActivity(intent);
    }

}


