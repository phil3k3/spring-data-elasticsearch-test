package com.test.model;

/**
 * Created by phil3k on 11.12.15.
 */
public class Book extends Entity {

    private String title;
    private String author;

    public Book() {

    }

    public Book(String title, String author, Long id) {
        super(id);
        this.title = title;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
