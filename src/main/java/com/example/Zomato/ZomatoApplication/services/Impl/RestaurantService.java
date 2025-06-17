package com.example.Zomato.ZomatoApplication.services.Impl;

import com.example.Zomato.ZomatoApplication.dtos.ItemsDto;
import com.example.Zomato.ZomatoApplication.dtos.RestaurantDto;

import java.util.List;

public interface RestaurantService {

    //Manages restaurant data & menus

    RestaurantDto createRestaurant(RestaurantDto restaurantDto);

    RestaurantDto getRestaurant(Long restaurantId);

    List<RestaurantDto> getAllRestaurant();

    RestaurantDto addItemsToRestaurant(Long restaurantId, List<ItemsDto> items);

    RestaurantDto removeItemsFromRestaurant(Long restaurantId, List<ItemsDto> items);

    RestaurantDto getRatingOfRestaurant(Long restaurantId);

    List<ItemsDto> getMenu(Long restaurantId, int page, int size, String sortBy);
}
