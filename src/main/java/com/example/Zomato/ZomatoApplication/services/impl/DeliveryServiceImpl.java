package com.example.Zomato.ZomatoApplication.services.impl;

import com.example.Zomato.ZomatoApplication.dtos.DeliveryDto;
import com.example.Zomato.ZomatoApplication.dtos.OrderDto;
import com.example.Zomato.ZomatoApplication.entites.DeliveryEntity;
import com.example.Zomato.ZomatoApplication.entites.DriverEntity;
import com.example.Zomato.ZomatoApplication.entites.OrderEntity;
import com.example.Zomato.ZomatoApplication.enums.DeliveryStatus;
import com.example.Zomato.ZomatoApplication.repositories.DeliveryRepository;
import com.example.Zomato.ZomatoApplication.repositories.DriverRepository;
import com.example.Zomato.ZomatoApplication.repositories.OrderRepository;
import com.example.Zomato.ZomatoApplication.services.DeliveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper mapper;

    @Override
    public DeliveryDto trackDelivery(Long orderId) {
        //TODO: Add support for Points to enable poll location feature on UI
        return null;
    }

    @Override
    public List<DeliveryDto> getAllDeliveriesByDriver(Long driverId) {
        DriverEntity driverEntity = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Invalid driver ID: " + driverId + "."));

        List<DeliveryEntity> deliveries = driverEntity.getDeliveries();
        return deliveries.stream()
                .map(delivery -> mapper.map(delivery, DeliveryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryDto getDeliveryInfo(Long deliveryId) {
        DeliveryEntity deliveryEntity = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Invalid Delivery ID: " + deliveryId + "."));
        return mapper.map(deliveryEntity, DeliveryDto.class);
    }

    @Override
    public OrderDto assignDriverToOrder(Long driverId, Long orderId) {
        DriverEntity driverEntity = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Invalid Driver ID: " + driverId + "."));

        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Invalid Order ID: " + orderId + "."));

        DeliveryEntity delivery = orderEntity.getDelivery();

        if (delivery == null) {
            delivery = new DeliveryEntity();
            delivery.setOrder(orderEntity);
            orderEntity.setDelivery(delivery);
        }

        delivery.setDriver(driverEntity);
        delivery.setStatus(DeliveryStatus.IN_PROGRESS);
        delivery.setDeliveryTime(LocalDateTime.now());

        OrderEntity saved = orderRepository.save(orderEntity);
        return mapper.map(saved, OrderDto.class);
    }


    @Override
    @Transactional
    public DeliveryDto updateDeliveryStatus(Long deliveryId, String status) {
        DeliveryEntity delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("No delivery found with ID: " + deliveryId));

        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new RuntimeException("This delivery is already marked as DELIVERED.");
        }

        DeliveryStatus newStatus;

        try {
            newStatus = DeliveryStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid delivery status: " + status);
        }

        delivery.setStatus(newStatus);

        if (newStatus == DeliveryStatus.DELIVERED) {
            delivery.setDeliveryTime(LocalDateTime.now());
        }

        DeliveryEntity updated = deliveryRepository.save(delivery);
        return mapper.map(updated, DeliveryDto.class);
    }

}
