package com.test.repository;

import com.test.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Created by phil3k on 11.12.15.
 */

public interface BookRepository extends ElasticsearchCrudRepository<Book, Long> {
}
