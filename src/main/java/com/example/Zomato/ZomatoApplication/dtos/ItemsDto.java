package com.example.Zomato.ZomatoApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDto {

    private Long id;
    private String name;
    private double price;
    private int quantity;
    private Long orderId;
    private Long restaurantId;

    public ItemsDto(Long id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
