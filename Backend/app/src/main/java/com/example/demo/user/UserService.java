package com.example.demo.user;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User[] users;
    public int currAmount;

    UserService()
    {
        users = new User[10];
        for(int i = 0; i < 10; i++)
        {
            users[i] = new User("John Doe", Integer.toString(i), "password123");
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

    public String createAndReturnUserInfo() {
        User user = new User("John Doe", "john.doe@example.com", "password123");
        return user.toString(); // Or return any specific detail
    }

}