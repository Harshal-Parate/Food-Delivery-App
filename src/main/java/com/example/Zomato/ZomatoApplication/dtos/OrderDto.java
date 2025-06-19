package com.example.Zomato.ZomatoApplication.dtos;

import com.example.Zomato.ZomatoApplication.enums.PaymentStatus;
import com.example.Zomato.ZomatoApplication.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private String orderTime;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private List<ItemsDto> items;
    private Long customerId;
}
