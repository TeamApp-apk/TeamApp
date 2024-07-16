package com.example.demo.user;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    public ArrayList<User> users;
    public int currAmount;

    UserService()
    {
        users = new ArrayList<User>();
        for(int i = 0; i < 10; i++)
        {


            users.add(new User("John Doe", Integer.toString(i), "password123"));
        }

        currAmount = 10;
    }

    public String getUserInfo(User user) {
        return "User Info: " + user.toString();
    }

    public User find(String login) {
        for (User user : users) {
            if (user.getName().equals(login)) {
                return user;
            }
        }
        return null;
    }


    public boolean LoginAuth(String login, String password) {
        User user = find(login);
        if (user != null) {
            return user.getPassword().equals(password);

        }
        return false;
    }

    public String createAndReturnUserInfo() {
        User user = new User("John Doe", "john.doe@example.com", "password123");
        return user.toString(); // Or return any specific detail
    }

}