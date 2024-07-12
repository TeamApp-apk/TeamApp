package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    // Existing method
    public String getUserInfo(User user) {
        return "User Info: " + user.toString();
    }

    // New method to create a User and return some information
    public String createAndReturnUserInfo() {
        User user = new User("John Doe", "john.doe@example.com", "password123");
        return user.toString(); // Or return any specific detail
    }
}