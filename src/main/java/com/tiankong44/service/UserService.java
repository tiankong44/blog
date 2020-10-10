package com.tiankong44.service;

import com.tiankong44.model.User;

public interface UserService {
    User findUser(String username, String password);
}
