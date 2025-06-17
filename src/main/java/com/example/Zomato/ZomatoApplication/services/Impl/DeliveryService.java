package com.example.Zomato.ZomatoApplication.services.Impl;

import com.example.Zomato.ZomatoApplication.dtos.DeliveryDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;

import java.util.List;

public interface DeliveryService {

    // Assigns & tracks deliveries

    DeliveryDto trackDelivery(Long orderId);

    List<DeliveryDto> getAllDeliveriesByDriver(Long driverId);

    DeliveryDto getDeliveryInfo(Long orderId);

    OrderDto assignDriverToOrder(Long driverId, Long orderId);

    DeliveryDto updateDeliveryStatus(Long deliveryId, String status);
}



