package com.flat.flat_management_backend.controller;

import com.flat.flat_management_backend.model.User;
import com.flat.flat_management_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // ✅ POST API
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // ✅ GET API
    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

}
