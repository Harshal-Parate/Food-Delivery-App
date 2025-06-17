package com.example.Zomato.ZomatoApplication.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<ItemsEntity> items;

    private double total;

    @OneToOne(mappedBy = "cart")
    private CustomerEntity customerEntity;
}
