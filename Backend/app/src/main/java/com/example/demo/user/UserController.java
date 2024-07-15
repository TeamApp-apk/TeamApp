package com.example.demo.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService = new UserService();

    @GetMapping("")
    List<User> getUsers() {
        return userService.users;
    }

    @GetMapping("/{id}")
    User getUserById(@PathVariable Integer id) {
        return userService.users.get(id);
    }

    @GetMapping("/auth")
    public boolean verifyUserCredentials(@RequestBody String[] array) {
        String login = array[0];
        String password = array[1];

        return userService.LoginAuth(array[0], array[1]);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/addUser")
    public void AddUser(@RequestBody String[] array) {
        userService.users.add(new User(array[0], array[1], array[2]));
    }
}
