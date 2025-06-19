package com.example.Zomato.ZomatoApplication.dtos;

import com.example.Zomato.ZomatoApplication.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {

    private Long id;
    private LocalDateTime deliveryTime;
    private DeliveryStatus status;
    private Long orderId;
    private Long driverId;
}

