package com.example.Zomato.ZomatoApplication.controllers;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.enums.Roles;
import com.example.Zomato.ZomatoApplication.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;

    @GetMapping(path = "/users")
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PostMapping(path = "/LogIn")
    public ResponseEntity<String> loginUser(@RequestBody UserDto user) {
        String accessToken = userService.login(user.getEmail(), user.getPassword());
        return new ResponseEntity<>(accessToken, HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<UserDto> onBoardUser(@RequestBody UserDto user) {
        UserDto newUser = userService.onBoardCustomer(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping(path = "/assign-role/{userId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable(value = "userId") Long userId,
                                              @RequestBody Roles role) {
        UserDto newUser = userService.assignRoleToUser(userId, role);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


}
