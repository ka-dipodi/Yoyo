package com.fgy.service;

import com.fgy.dao.UserRepository;
import com.fgy.po.User;
import com.fgy.util.MD5utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserSerivce{

    @Autowired
    private UserRepository userRepository;

    /*验证用户*/
    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5utils.code(password));
        return user;
    }
}
