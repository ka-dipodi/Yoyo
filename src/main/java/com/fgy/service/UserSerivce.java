package com.fgy.service;

import com.fgy.po.User;

public interface UserSerivce {

    User checkUser(String username, String password);
}
