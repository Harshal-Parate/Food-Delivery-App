package com.example.Zomato.ZomatoApplication.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {

    private Long id;
    private String name;
    private String address;
    private List<ItemsDto> menuItems;
    private Double rating;

    public RestaurantDto(Double rating) {
        this.rating = rating;
    }
}
