package com.test.service;

import com.test.model.User;
import com.test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserSearch {

    @Autowired
    private UserRepository repository;

    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public Iterable<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void index(User user) {
        repository.save(user);
    }
}
