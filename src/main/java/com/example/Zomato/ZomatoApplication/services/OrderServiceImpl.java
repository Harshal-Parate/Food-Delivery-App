package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.CartDto;
import com.example.Zomato.ZomatoApplication.dtos.CustomerDto;
import com.example.Zomato.ZomatoApplication.dtos.ItemsDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;
import com.example.Zomato.ZomatoApplication.entites.*;
import com.example.Zomato.ZomatoApplication.enums.PaymentStatus;
import com.example.Zomato.ZomatoApplication.enums.PaymentType;
import com.example.Zomato.ZomatoApplication.repositories.*;
import com.example.Zomato.ZomatoApplication.services.Impl.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public CustomerDto addItemToCart(Long customerId, Long itemId, int qty) {

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("No Customer exists by ID: " + customerId));

        ItemsEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("No Item exists by ID: " + itemId));

        if (item.getQuantity() <= 0) {
            throw new RuntimeException("Selected item is out of stock");
        }

        int availableQty = item.getQuantity();
        if (availableQty <= qty) {
            item.setQuantity(0);
        } else {
            item.setQuantity(availableQty - qty);
        }

        itemRepository.save(item);

        // Create a new item instance to add to the cart
        ItemsEntity cartItem = new ItemsEntity();
        cartItem.setName(item.getName());
        cartItem.setPrice(item.getPrice());
        cartItem.setQuantity(qty);
        cartItem.setRestaurant(item.getRestaurant());

        CartEntity cart = customer.getCart();
        if (cart == null) {
            cart = new CartEntity();
            cart.setItems(new ArrayList<>());
            cart.setTotal(0);
            cart.setCustomer(customer);
            customer.setCart(cart);
        }
        ItemsEntity savedCartItem = itemRepository.save(cartItem);
        cart.getItems().add(savedCartItem);
        cart.setTotal(cart.getTotal() + cartItem.getPrice() * qty);

        customerRepository.save(customer);
        return mapper.map(customer, CustomerDto.class);
    }

/*
    @Override
    public CartDto removeItemFromCart(Long customerId, int qty, String itemName) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("No Customer exists by ID: " + customerId));

        List<ItemsEntity> items = customer.getCart().getItems();
        List<ItemsEntity> finalList = items.stream()
                .filter(item -> Objects.equals(item.getName(), itemName))
                .map(filteredItem -> {
                    int existingQuantity = filteredItem.getQuantity();
                    filteredItem.setQuantity(existingQuantity - qty);
                    return itemRepository.save(filteredItem);
                })
                .toList();

        customer.getCart().setItems(finalList);
        CustomerEntity modified = customerRepository.save(customer);
        return mapper.map(modified.getCart(), CartDto.class);
    }
 */

    @Override
    @Transactional
    public CartDto removeItemFromCart(Long customerId, int qty, String itemName) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("No Customer exists by ID: " + customerId));

        CartEntity cart = customer.getCart();
        if (cart == null || cart.getItems() == null) {
            throw new RuntimeException("Cart is empty");
        }

        List<ItemsEntity> items = cart.getItems();
        Iterator<ItemsEntity> iterator = items.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            ItemsEntity item = iterator.next();
            if (item.getName().equalsIgnoreCase(itemName)) {
                found = true;

                if (qty >= item.getQuantity()) {
                    cart.setTotal(cart.getTotal() - (item.getPrice() * item.getQuantity()));
                    iterator.remove();// Remove item
                } else {
                    item.setQuantity(item.getQuantity() - qty);
                    cart.setTotal(cart.getTotal() - (item.getPrice() * qty));
                }
                break;
            }
        }

        if (!found) {
            throw new RuntimeException("Item with name " + itemName + " not found in cart.");
        }

        cart.setItems(items);
        customer.setCart(cart);
        CustomerEntity updated = customerRepository.save(customer);
        return mapper.map(updated.getCart(), CartDto.class);
    }

    @Override
    public CartDto getCartDetails(Long cartId) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));

        return mapper.map(cart, CartDto.class);
    }

    @Override
    @Transactional
    public OrderDto checkoutCart(Long cartId) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with ID: " + cartId));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }

        CustomerEntity customer = cart.getCustomer();
        if (customer == null) {
            throw new RuntimeException("Cart is not associated with a customer.");
        }

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setItems(new ArrayList<>(cart.getItems()));
        order.setOrderTime(LocalDateTime.now());
        order.setPaymentStatus(PaymentStatus.PENDING); // or DEFAULT
        order.setPaymentType(PaymentType.CASH);         // or DEFAULT

        for (ItemsEntity item : cart.getItems()) {
            item.setOrder(order);
        }

        OrderEntity savedOrder = orderRepository.save(order);

        // Clear the cart after checkout
        cart.setItems(new ArrayList<>());
        cart.setTotal(0);
        cartRepository.save(cart);

        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public List<ItemsDto> getAllOrdersOfCustomerForRestaurant(Long restaurantId, Long customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("No Customer exists by ID: " + customerId));

        return customer.getOrders()
                .stream()
                .flatMap(order -> order.getItems()
                        .stream()
                        .filter(item -> Objects.equals(item.getRestaurant().getId(), restaurantId)))
                .map(item -> new ItemsDto(
                        item.getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getPrice()))
                .collect(Collectors.toList());
    }
}


