package com.example.Zomato.ZomatoApplication.services.Impl;

import com.example.Zomato.ZomatoApplication.dtos.CartDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;

public interface OrderService {

    // Handles cart, orders, delivery

    CartDto addItemToCart(Long cartId, Long itemId);

    CartDto removeItemFromCart(Long cartId, Long itemId);

    CartDto getCartDetails(Long cartId);

    OrderDto checkoutCart(Long cartId);
}
