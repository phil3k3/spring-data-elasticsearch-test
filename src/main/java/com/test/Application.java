package com.test;

import com.test.model.User;
import com.test.service.UserSearch;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String args[])
    {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        UserSearch search = context.getBean(UserSearch.class);
        search.index(new User("johnny", "walker", 1L));
        Iterable<User> users = search.findAll();
        for(User user : users) {
            System.out.println(user.getFirstName()  + " " + user.getSurname());
        }
    }
}
