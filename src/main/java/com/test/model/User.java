package com.test.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="userstest")
public class User {

    private Long id;
    private String firstName;
    private String surname;

    public User(String firstName, String surname, Long id) {
        this.firstName = firstName;
        this.surname = surname;
        this.id = id;
    }

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
