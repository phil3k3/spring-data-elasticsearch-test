package com.test.service;

import com.test.model.User;

public interface UserSearch {

    Iterable<User> findAll();

    void index(User user);
}
