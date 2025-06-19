package com.example.Zomato.ZomatoApplication.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;
    private String name;
    private String email;
    private Long walletId;
    private Long cartId;
}
