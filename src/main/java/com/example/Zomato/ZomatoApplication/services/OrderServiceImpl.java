package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.CartDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;
import com.example.Zomato.ZomatoApplication.services.Impl.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public CartDto addItemToCart(Long cartId, Long itemId) {
        return null;
    }

    @Override
    public CartDto removeItemFromCart(Long cartId, Long itemId) {
        return null;
    }

    @Override
    public CartDto getCartDetails(Long cartId) {
        return null;
    }

    @Override
    public OrderDto checkoutCart(Long cartId) {
        return null;
    }
}
