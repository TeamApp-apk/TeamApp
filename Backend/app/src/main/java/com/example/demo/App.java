package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

@SpringBootApplication
@RestController
public class App {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(App.class);

    @Autowired
    private UserService userService;
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        log.info("Application started");
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        String userInfo = userService.createAndReturnUserInfo();
        return String.format("Hello %s! Here a user info: %s", name, userInfo);
    }
}