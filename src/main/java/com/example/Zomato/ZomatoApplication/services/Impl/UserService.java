package com.example.Zomato.ZomatoApplication.services.Impl;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.enums.Roles;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto onBoardCustomer(UserDto user);

    UserDto assignRoleToUser(Long userId, Roles role);

    String login(String username, String password);
}