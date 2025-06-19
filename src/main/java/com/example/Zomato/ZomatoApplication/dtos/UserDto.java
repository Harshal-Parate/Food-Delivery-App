package com.example.Zomato.ZomatoApplication.dtos;

import com.example.Zomato.ZomatoApplication.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Roles role;
    
}
