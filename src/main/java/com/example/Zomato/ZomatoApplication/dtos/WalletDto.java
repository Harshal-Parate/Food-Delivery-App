package com.example.Zomato.ZomatoApplication.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {

    private Long id;
    private double balance;
    private LocalDateTime createdAt;
    private CustomerDto customer;
}
