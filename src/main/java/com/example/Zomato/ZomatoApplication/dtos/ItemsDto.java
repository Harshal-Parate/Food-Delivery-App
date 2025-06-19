package com.example.Zomato.ZomatoApplication.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
}
