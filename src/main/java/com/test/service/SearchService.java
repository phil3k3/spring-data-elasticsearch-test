package com.test.service;

import com.test.model.Book;
import com.test.model.Entity;
import com.test.model.User;
import com.test.repository.BookRepository;
import com.test.repository.UserRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements Search {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private HierarchyResultsMapper mapper;

    @Override
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<Book> findAllBooks() {
        QueryBuilder query = QueryBuilders.matchAllQuery();
        return template.queryForList(new NativeSearchQuery(query), Book.class);
    }

    @Override
    public Iterable<Entity> findAllEntities() {
        QueryBuilder query = QueryBuilders.matchAllQuery();
        SearchQuery nativeQuery = new NativeSearchQuery(query);
        nativeQuery.addTypes(mapper.getTypes(Entity.class));
        return template.queryForList(nativeQuery, Entity.class);
    }

    @Override
    public void index(User user) {
        userRepository.save(user);
    }

    @Override
    public void index(Book book) {
        bookRepository.save(book);
    }
}
