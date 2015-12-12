package com.test.service;

import com.test.model.Book;
import com.test.model.Entity;
import com.test.model.User;

public interface Search {

    Iterable<User> findAllUsers();
    Iterable<Book> findAllBooks();
    Iterable<Entity> findAllEntities();

    void index(User user);

    void index(Book book);
}
