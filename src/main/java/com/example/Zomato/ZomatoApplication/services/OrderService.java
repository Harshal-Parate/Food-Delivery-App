package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.CartDto;
import com.example.Zomato.ZomatoApplication.dtos.CustomerDto;
import com.example.Zomato.ZomatoApplication.dtos.ItemsDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;

import java.util.List;

public interface OrderService {

    // Handles cart, orders, delivery

    CustomerDto addItemToCart(Long customerId, Long itemId, int qty);

    CartDto removeItemFromCart(Long customerId, int qty, String itemName);

    CartDto getCartDetails(Long cartId);

    OrderDto checkoutCart(Long cartId);

    List<ItemsDto> getAllOrdersOfCustomerForRestaurant(Long restaurantId, Long customerId);
}
