package com.example.Zomato.ZomatoApplication.entites;

import com.example.Zomato.ZomatoApplication.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Items> items;

    private double total;

    @OneToOne(mappedBy = "cart")
    private Customer customer;
}
