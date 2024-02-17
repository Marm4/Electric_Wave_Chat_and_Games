package com.marm4.electric_wave.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.marm4.electric_wave.Adapter.ChatRecyclerViewAdapter;
import com.marm4.electric_wave.Adapter.GroupChatRecyclerViewAdapter;
import com.marm4.electric_wave.Interface.OnChatCompleteListener;
import com.marm4.electric_wave.R;
import com.marm4.electric_wave.controller.GroupChatController;
import com.marm4.electric_wave.global.CurrentChat;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.GroupChat;
import com.marm4.electric_wave.model.Message;
import com.marm4.electric_wave.model.User;

import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText textET;
    private TextView nameTV;
    private ImageView sendIV;
    private User friend;
    private User currentUser;
    private GroupChat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initUI();
    }

    private void initUI() {
        textET = findViewById(R.id.text);
        nameTV = findViewById(R.id.name);
        sendIV = findViewById(R.id.send);

        friend = CurrentChat.getInstance().getUser();
        currentUser = CurrentUser.getInstance().getUser();
        chat = CurrentChat.getInstance().getGroupChat();

        messageAction();
        showChat(chat.getMessages());
    }

    private void messageAction() {
        nameTV.setText(friend.getUserName());
        sendIV.setOnClickListener(view -> {
            String messageString = textET.getText().toString();
            if (!messageString.isEmpty()) {
                Message message = new Message(currentUser.getId(), friend.getId(), messageString, new Date());
                chat.addMessages(message);
                saveChat();
            }
        });
    }

    private void saveChat() {
        GroupChatController groupChatController = new GroupChatController();
        groupChatController.saveGroupChat(chat);
    }

    private void showChat(List<Message> chat){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        ChatRecyclerViewAdapter adapter = new ChatRecyclerViewAdapter(chat);
        recyclerView.setAdapter(adapter);
    }
}