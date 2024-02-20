package com.marm4.electric_wave.global;

import androidx.recyclerview.widget.RecyclerView;

import com.marm4.electric_wave.Adapter.ChatRecyclerViewAdapter;
import com.marm4.electric_wave.Adapter.GroupChatRecyclerViewAdapter;
import com.marm4.electric_wave.Adapter.NotificationsRecyclerViewAdapter;
import com.marm4.electric_wave.Adapter.SearchRecyclerViewAdapter;
import com.marm4.electric_wave.model.Message;

import java.util.List;

public class CurrentAdapters {
    private static CurrentAdapters instance;
    private ChatRecyclerViewAdapter chatAdapter;
    private GroupChatRecyclerViewAdapter groupAdapter;
    private NotificationsRecyclerViewAdapter notificationsAdapter;
    private SearchRecyclerViewAdapter searchAdapter;
    private RecyclerView chatView;

    public static CurrentAdapters getInstance(){
        if(instance == null){
            instance = new CurrentAdapters();
        }
        return instance;
    }

    public void setChatAdapter(ChatRecyclerViewAdapter chatAdapter, RecyclerView view) {
        this.chatAdapter = chatAdapter;
        this.chatView = view;
    }

    public void setGroupAdapter(GroupChatRecyclerViewAdapter groupAdapter) {
        this.groupAdapter = groupAdapter;
    }

    public void setNotificationsAdapter(NotificationsRecyclerViewAdapter notificationsAdapter) {
        this.notificationsAdapter = notificationsAdapter;
    }

    public void setSearchAdapter(SearchRecyclerViewAdapter searchAdapter) {
        this.searchAdapter = searchAdapter;
    }

    public ChatRecyclerViewAdapter getChatAdapter() {
        return chatAdapter;
    }

    public GroupChatRecyclerViewAdapter getGroupAdapter() {
        return groupAdapter;
    }

    public NotificationsRecyclerViewAdapter getNotificationsAdapter() {
        return notificationsAdapter;
    }

    public SearchRecyclerViewAdapter getSearchAdapter() {
        return searchAdapter;
    }

    public void notifyChangeChat(List<Message> messageList){
        chatAdapter.notifyDataSetChanged();
        chatView.smoothScrollToPosition(messageList.size()-1);
    }

    public void destroy(){
        chatAdapter = null;
        groupAdapter = null;
        searchAdapter = null;
        notificationsAdapter = null;
    }
}
