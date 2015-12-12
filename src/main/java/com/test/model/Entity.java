package com.test.model;

import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Created by phil3k on 11.12.15.
 */
@Document(indexName="userstest")
public abstract class Entity {
    protected Long id;

    public Entity() {

    }

    public Entity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
