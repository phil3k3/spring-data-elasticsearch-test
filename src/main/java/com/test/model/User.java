package com.test.model;

public class User extends Entity {

    private String firstName;
    private String surname;

    public User(String firstName, String surname, Long id) {
        super(id);
        this.firstName = firstName;
        this.surname = surname;
    }

    public User() {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

}
