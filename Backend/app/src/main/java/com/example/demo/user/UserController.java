package com.example.demo.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService = new UserService();

    @GetMapping("")
    User[] getUsers() {
        return userService.users;
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable Integer id) {
        return userService.users[id];
    }

    @GetMapping("/auth")
    public boolean verifyUserCredentials(@RequestParam String login, @RequestParam String password) {
        User user = userService.find(login);
        if (user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

}
