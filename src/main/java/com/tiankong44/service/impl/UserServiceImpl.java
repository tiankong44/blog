package com.tiankong44.service.impl;

import com.tiankong44.dao.UserMapper;
import com.tiankong44.model.User;
import com.tiankong44.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author 12481
 * @Date 16:57
 * @Version 1.0
 **/

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUser(String username, String password) {
        User user = userMapper.findUser(username, password);

        return user;
    }
}
