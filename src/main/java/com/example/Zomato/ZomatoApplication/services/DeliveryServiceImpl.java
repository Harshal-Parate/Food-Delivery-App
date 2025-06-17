package com.example.Zomato.ZomatoApplication.services;

import com.example.Zomato.ZomatoApplication.dtos.DeliveryDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;
import com.example.Zomato.ZomatoApplication.services.Impl.DeliveryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Override
    public DeliveryDto trackDelivery(Long orderId) {
        return null;
    }

    @Override
    public List<DeliveryDto> getAllDeliveriesByDriver(Long driverId) {
        return null;
    }

    @Override
    public DeliveryDto getDeliveryInfo(Long orderId) {
        return null;
    }

    @Override
    public OrderDto assignDriverToOrder(Long driverId, Long orderId) {
        return null;
    }

    @Override
    public DeliveryDto updateDeliveryStatus(Long deliveryId, String status) {
        return null;
    }
}
