package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.enums.Roles;
import com.example.Zomato.ZomatoApplication.services.impl.JwtService;
import com.example.Zomato.ZomatoApplication.services.impl.SessionService;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto onBoardCustomer(UserDto user);

    UserDto assignRoleToUser(Long userId, String role);

    UserDto createUser(UserDto userDto);

    UserDto refreshToken(String refreshToken);

    UserDto authenticateUser(UserDto userDto);
}
