package com.example.Zomato.ZomatoApplication.controllers;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.enums.Roles;
import com.example.Zomato.ZomatoApplication.services.impl.UserServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    @PostMapping(path = "/Login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto loginDto,
                                       HttpServletResponse response) {
        UserDto userDto = userService.authenticateUser(loginDto);

        Cookie cookie = new Cookie("refreshToken", userDto.getRefreshToken());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        response.addCookie(cookie);

        return new ResponseEntity<>(userDto.getAccessToken(), HttpStatus.OK);
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<UserDto> createNewUser(@RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<UserDto> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        String refreshToken = Arrays.stream(cookies)
                .filter(token -> token.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found"));

        return new ResponseEntity<>(userService.refreshToken(refreshToken), HttpStatus.OK);
    }

    @PutMapping(path = "/assign-role/{userId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable(value = "userId") Long userId,
                                              @RequestBody Roles role) {
        UserDto newUser = userService.assignRoleToUser(userId, role);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


}
