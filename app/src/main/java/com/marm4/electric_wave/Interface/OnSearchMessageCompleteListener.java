package com.marm4.electric_wave.Interface;

import com.marm4.electric_wave.model.Message;

import java.util.List;

public interface OnSearchMessageCompleteListener {

    void onSearchMessageComplete(List<Message> messageList);

    void onSearchMessageError(String errorMessage);
}
