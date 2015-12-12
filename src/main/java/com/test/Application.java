package com.test;

import com.test.model.Book;
import com.test.model.Entity;
import com.test.model.User;
import com.test.service.Search;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String args[])
    {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        Search search = context.getBean(Search.class);
        search.index(new User("johnny", "walker", 1L));
        Iterable<User> users = search.findAllUsers();
        for(User user : users) {
            System.out.println(user.getFirstName()  + " " + user.getSurname());
        }

        search.index(new Book("my book", "the author", 1L));
        Iterable<Book> books = search.findAllBooks();
        for(Book book : books) {
            System.out.println(book.getTitle()  + " " + book.getAuthor());
        }

        Iterable<Entity> entities = search.findAllEntities();
        for(Entity entity : entities) {
            System.out.println(entity.getId() + " " + entity.getClass());
        }
    }
}
