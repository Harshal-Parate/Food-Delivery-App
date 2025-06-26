package com.example.Zomato.ZomatoApplication.controllers;

import com.example.Zomato.ZomatoApplication.dtos.CartDto;
import com.example.Zomato.ZomatoApplication.dtos.CustomerDto;
import com.example.Zomato.ZomatoApplication.dtos.ItemsDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;
import com.example.Zomato.ZomatoApplication.services.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping(path = "/{restaurantId}/{customerId}")
    public ResponseEntity<List<ItemsDto>> getAllOrdersOfCustomerAccordingToRestaurant(@PathVariable(value = "restaurantId") Long restaurantId,
                                                                                      @PathVariable(value = "customerId") Long customerId) {
        List<ItemsDto> allOrdersOfCustomerForRestaurant = orderService.getAllOrdersOfCustomerForRestaurant(restaurantId, customerId);
        return new ResponseEntity<>(allOrdersOfCustomerForRestaurant, HttpStatus.OK);
    }

    @PutMapping(path = "/{customerId}/{itemId}")
    public ResponseEntity<CustomerDto> addItemToCart(@PathVariable(value = "customerId") Long customerId,
                                                     @PathVariable(value = "itemId") Long itemId,
                                                     @RequestParam(defaultValue = "1") int qty) {
        CustomerDto customerDto = orderService.addItemToCart(customerId, itemId, qty);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<CartDto> removeItemToCart(@PathVariable(value = "customerId") Long customerId,
                                                    @RequestParam(defaultValue = "1") int qty,
                                                    @RequestParam(defaultValue = "") String itemName) {
        CartDto cartDto = orderService.removeItemFromCart(customerId, qty, itemName);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @GetMapping(path = "/{cartId}")
    public ResponseEntity<CartDto> getCartDetails(@PathVariable Long cartId) {
        CartDto cartDetails = orderService.getCartDetails(cartId);
        return ResponseEntity.ok(cartDetails);
    }

    @PostMapping(path = "/{cartId}/checkout")
    public ResponseEntity<OrderDto> checkoutCart(@PathVariable Long cartId) {
        OrderDto orderDto = orderService.checkoutCart(cartId);
        return ResponseEntity.ok(orderDto);
    }

}
