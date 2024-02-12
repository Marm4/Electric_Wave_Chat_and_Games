package com.marm4.electric_wave.Interface;

import com.marm4.electric_wave.model.User;

import java.util.List;

public interface OnSearchUserCompleteListener {
    void onSearchUserComplete(List<User> userList);
    void onSearchUserError(String errorMessage);


}
